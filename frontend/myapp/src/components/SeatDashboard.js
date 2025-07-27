import React, { useState, useEffect, useCallback } from 'react';
import axios from 'axios';
import './SeatDashboard.css';

const api = axios.create({
  baseURL: 'http://localhost:8080',
  headers: {'Content-Type': 'application/json'}
});

api.interceptors.request.use(config => {
  const credentials = localStorage.getItem('credentials');
  if (credentials) {
    config.headers.Authorization = `Basic ${credentials}`;
  }
  return config;
});

function SeatDashboard({ user }) {
  const [seats, setSeats] = useState([]);
  const [selectedSeats, setSelectedSeats] = useState([]);
  const [requests, setRequests] = useState([]);
  const [error, setError] = useState(null);
  const [approvedSeats, setApprovedSeats] = useState(new Map());
  const [requestForm, setRequestForm] = useState({
    startDate: '',
    endDate: ''
  });

  const fetchSeats = async () => {
    try {
        const seatsResponse = await api.get('/api/seat-request/getAllSeats');
        let allSeats = seatsResponse.data || [];

        const statusResponse = await api.get('/api/seat-request/getAllSeatStatus');
        const seatStatuses = statusResponse.data || [];

        const statusMap = new Map();
        seatStatuses.forEach(status => {
            statusMap.set(status.seatNo, {
                status: status.status,
                employeeId: status.employeeId,
                occupantName: status.occupantName,  // Add this
                projectName: status.projectName     // Add this
            });
        });

        allSeats = allSeats.map(seat => ({
            ...seat,
            status: statusMap.get(seat.seatNo)?.status || null,
            approvedFor: statusMap.get(seat.seatNo)?.employeeId || null,
            occupantName: statusMap.get(seat.seatNo)?.occupantName || seat.occupantName,
            projectName: statusMap.get(seat.seatNo)?.projectName || seat.projectName
        }));

        setSeats(allSeats);
        setApprovedSeats(statusMap); 
        setError(null);
    } catch (err) {
        console.error('Error fetching seats:', err);
        setError('Failed to fetch seats');
    }
};

  const fetchRequests = useCallback(async () => {
    try {
      let endpoint;
      if (user.role === 'ROLE_ADMIN') {
        endpoint = '/api/seat-approval/pendingRequestByManager';
      } else if (user.role === 'ROLE_MANAGER') {
        const [singleResponse, multipleResponse] = await Promise.all([
          api.get('/api/seat-approval/pendingSeatForApproval'),
          api.get('/api/seat-request/allRequestByManager')
        ]);

        const singleRequests = singleResponse.data || [];
        const multipleRequests = multipleResponse.data || [];
        
        const allRequests = [...singleRequests, ...multipleRequests]
          .filter(req => req.status === 'PENDING');

        setRequests(allRequests);
        return;
      } else {
        endpoint = '/api/seat-request/allRequestByDeveloper';
      }

      const response = await api.get(endpoint);
      setRequests(response.data || []);
    } catch (err) {
      console.error('Error fetching requests:', err);
      setError('Failed to fetch requests');
    }
  }, [user.role]);

  const handleSeatClick = (seatId) => {
    const seat = seats.find(s => s.seatNo === seatId);
    const seatStatus = approvedSeats.get(seatId);
    
    if (user.role === 'ROLE_MANAGER') {
      if (seatStatus?.status === 'APPROVED') {
        if (selectedSeats.length >= 1 && !selectedSeats.includes(seatId)) {
          setError('Please request one approved seat at a time');
          return;
        }
      }
    } else if (user.role === 'ROLE_DEVELOPER') {
      if (selectedSeats.length >= 1 && !selectedSeats.includes(seatId)) {
        setError('Developers can only select one seat at a time');
        return;
      }
      if (seatStatus?.status !== 'APPROVED') {
        setError('This seat is not available for request');
        return;
      }
    }

    setSelectedSeats(prev => 
      prev.includes(seatId) ? prev.filter(id => id !== seatId) : [...prev, seatId]
    );
    setError(null);
  };

  const handleRequestSubmit = async () => {
    try {
      if (!requestForm.startDate || !requestForm.endDate) {
        setError('Please select both start and end dates');
        return;
      }

      if (user.role === 'ROLE_MANAGER' && selectedSeats.length === 1) {
        const seatNo = selectedSeats[0];
        const seatStatus = approvedSeats.get(seatNo);
        
        if (seatStatus?.status === 'APPROVED') {
          await api.put('/api/seat-request/getSeatForManager', null, {
            params: {
              seatNo: selectedSeats[0],
              employeeId: parseInt(user.id)
            }
          });
        } else {
          await api.post('/api/seat-request/raiseMultipleSeatRequest', {
            employeeId: parseInt(user.id),
            startDate: requestForm.startDate,
            endDate: requestForm.endDate,
            seatNumbers: selectedSeats,
            status: 'PENDING'
          });
        }
      } else if (user.role === 'ROLE_MANAGER' && selectedSeats.length > 1) {
        await api.post('/api/seat-request/raiseMultipleSeatRequest', {
          employeeId: parseInt(user.id),
          startDate: requestForm.startDate,
          endDate: requestForm.endDate,
          seatNumbers: selectedSeats,
          status: 'PENDING'
        });
      } else if (user.role === 'ROLE_DEVELOPER') {
        await api.post('/api/seat-request/raiseRequestForDeveloper', {
          employeeId: parseInt(user.id),
          startDate: requestForm.startDate,
          endDate: requestForm.endDate,
          seatNo: selectedSeats[0],
          status: 'PENDING'
        });
      }

      setSelectedSeats([]);
      setRequestForm({ startDate: '', endDate: '' });
      setError(null);
      await Promise.all([fetchRequests(), fetchSeats()]);
    } catch (err) {
      console.error('Error details:', err.response?.data);
      setError(err.response?.data || 'Failed to submit request');
    }
  };

  const handleApproval = async (requestId, status, seatNumbers) => {
    try {
      const endpoint = `/api/seat-approval/${user.role === 'ROLE_MANAGER' ? 'approve' : 'approveManagerRequest'}/${requestId}`;
      await api.post(`${endpoint}?status=${status}`);

      setRequests(prev => prev.filter(req => req.id !== requestId));
      await fetchSeats();
      setError(null);
    } catch (err) {
      console.error('Error in approval:', err);
      setError('Failed to process approval');
    }
  };

  useEffect(() => {
    fetchSeats();
    fetchRequests();
  }, [fetchRequests]);

  // Initialize empty seats array for fixed layout
  const allSeats = [...Array(90)].map((_, index) => ({
    seatNo: (index + 1).toString(),
    available: true,
    ...seats.find(s => s.seatNo === (index + 1).toString())
  }));

  // Split into sections of 10
  const dividedSeats = [];
  for (let i = 0; i < allSeats.length; i += 10) {
    dividedSeats.push(allSeats.slice(i, Math.min(i + 10, allSeats.length)));
  }

  function splitSeats(seatArray) {
    if (seatArray.length > 5) {
      return [seatArray.slice(0, 5), seatArray.slice(5)];
    }
    return [seatArray, []];
  }
  return (
    <div className="dashboard">
      <div className="header">
        <h2>Seat Management Dashboard</h2>
        <div className="user-info">
          <p>Welcome {user.name}</p>
          <p>Role: {user.role.replace('ROLE_', '')}</p>
          <p>Project: {user.projectName}</p>
        </div>
      </div>
 
      {error && <div className="error-message">{error}</div>}
 
      <div className="floor-plan">
        {dividedSeats.map((element, index) => {
          let [firstHalf, secondHalf] = splitSeats(element);
          let hasPillar = index % 2 === 0;
 
          return (
            <div key={index} className="seat-section">
              {/* First Half */}
              <div className="first-half">
                {firstHalf.map((seat) => (
                  <div
                    key={seat.seatNo}
                    className={`seat ${
                      approvedSeats.get(seat.seatNo)?.status?.toLowerCase() || 'available'
                    } ${selectedSeats.includes(seat.seatNo) ? 'selected' : ''}`}
                    onClick={() => handleSeatClick(seat.seatNo)}
                  >
                    {seat.seatNo}
                    {seat.occupantName && (
                      <div className="occupant-info">
                        <div className="occupant-name">{seat.occupantName}</div>
                        <div className="project-name">{seat.projectName}</div>
                      </div>
                    )}
                    {approvedSeats.get(seat.seatNo) && (
                      <div className="status-badge">
                        {approvedSeats.get(seat.seatNo).status}
                      </div>
                    )}
                  </div>
                ))}
              </div>
 
              {/* Second Half */}
              <div className="second-half">
                {hasPillar ? (
                  <>
                    <div className="pillar">P</div>
                    {secondHalf.slice(1).map((seat) => (
                      <div
                        key={seat.seatNo}
                        className={`seat ${
                          approvedSeats.get(seat.seatNo)?.status?.toLowerCase() || 'available'
                        } ${selectedSeats.includes(seat.seatNo) ? 'selected' : ''}`}
                        onClick={() => handleSeatClick(seat.seatNo)}
                      >
                        {seat.seatNo}
                        {seat.occupantName && (
                          <div className="occupant-info">
                            <div className="occupant-name">{seat.occupantName}</div>
                            <div className="project-name">{seat.projectName}</div>
                          </div>
                        )}
                        {approvedSeats.get(seat.seatNo) && (
                          <div className="status-badge">
                            {approvedSeats.get(seat.seatNo).status}
                          </div>
                        )}
                      </div>
                    ))}
                  </>
                ) : (
                  secondHalf.map((seat) => (
                    <div
                      key={seat.seatNo}
                      className={`seat ${
                        approvedSeats.get(seat.seatNo)?.status?.toLowerCase() || 'available'
                      } ${selectedSeats.includes(seat.seatNo) ? 'selected' : ''}`}
                      onClick={() => handleSeatClick(seat.seatNo)}
                    >
                      {seat.seatNo}
                      {seat.occupantName && (
                        <div className="occupant-info">
                          <div className="occupant-name">{seat.occupantName}</div>
                          <div className="project-name">{seat.projectName}</div>
                        </div>
                      )}
                      {approvedSeats.get(seat.seatNo) && (
                        <div className="status-badge">
                          {approvedSeats.get(seat.seatNo).status}
                        </div>
                      )}
                    </div>
                  ))
                )}
              </div>
            </div>
          );
        })}
 
        {selectedSeats.length > 0 && (
          <div className="request-form">
            <h3>
              {user.role === 'ROLE_MANAGER' && selectedSeats.length === 1 
                ? 'Request Single Seat' 
                : user.role === 'ROLE_MANAGER' 
                  ? 'Request Multiple Seats' 
                  : 'Request Seat'}
            </h3>
            <div className="form-group">
              <label>Start Date:</label>
              <input
                type="date"
                value={requestForm.startDate}
                onChange={e => setRequestForm({...requestForm, startDate: e.target.value})}
                min={new Date().toISOString().split('T')[0]}
              />
            </div>
            <div className="form-group">
              <label>End Date:</label>
              <input
                type="date"
                value={requestForm.endDate}
                onChange={e => setRequestForm({...requestForm, endDate: e.target.value})}
                min={requestForm.startDate || new Date().toISOString().split('T')[0]}
              />
            </div>
            <button onClick={handleRequestSubmit} className="submit-btn">
              Submit Request
            </button>
          </div>
        )}
      </div>
 
      {requests.length > 0 && (
        <div className="requests-section">
          <h3>Pending Requests</h3>
          <div className="requests-list">
            {requests.map(request => (
              <div key={request.id} className="request-card">
                <p><strong>Seat(s):</strong> {request.seatNo || request.seatNumbers?.join(', ')}</p>
                <p><strong>Employee ID:</strong> {request.employeeId}</p>
                <p><strong>From:</strong> {new Date(request.startDate).toLocaleDateString()}</p>
                <p><strong>To:</strong> {new Date(request.endDate).toLocaleDateString()}</p>
                <p><strong>Project:</strong> {request.projectName}</p>
                <p><strong>Requestor:</strong> {request.occupantName}</p>
                {((user.role === 'ROLE_MANAGER' && (!request.seatNumbers || request.seatNumbers.length === 1)) ||
                  (user.role === 'ROLE_ADMIN' && request.seatNumbers?.length > 1)) && 
                 request.status === 'PENDING' && (
                  <div className="action-buttons">
                    <button 
                      onClick={() => handleApproval(request.id, 'APPROVED', request.seatNo || request.seatNumbers)}
                      className="approve-btn"
                    >
                      Approve
                    </button>
                    <button 
                      onClick={() => handleApproval(request.id, 'REJECTED', request.seatNo || request.seatNumbers)}
                      className="reject-btn"
                    >
                      Reject
                    </button>
                  </div>
                )}
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );

}

export default SeatDashboard;
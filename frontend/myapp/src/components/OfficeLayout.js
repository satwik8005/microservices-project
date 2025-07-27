import React, { useState, useEffect } from 'react';

const OfficeLayout = () => {
  // Initialize states for seats, conference rooms, and booking details
  const [seats, setSeats] = useState([]);
  const [conferenceRooms, setConferenceRooms] = useState([]);
  const [selectedSpace, setSelectedSpace] = useState(null);
  const [showDialog, setShowDialog] = useState(false);
  const [employeeDetails, setEmployeeDetails] = useState({
    name: '',
    email: '',
    department: '',
    employeeId: ''
  });
  const [bookingDetails, setBookingDetails] = useState({
    employeeName: '',
    department: '',
    date: '',
    startTime: '',
    endTime: ''
  });

  useEffect(() => {
    const fetchData = () => {
      // Fetch seats from the backend API
      fetch('http://localhost:8080/api/seats')
        .then(response => response.json())
        .then(data => setSeats(data))
        .catch(error => console.error('Error fetching seats:', error));
      
      // Fetch conference rooms from the backend API
      fetch('http://localhost:8080/api/conference-rooms')
        .then(response => response.json())
        .then(data => setConferenceRooms(data))
        .catch(error => console.error('Error fetching rooms:', error));
    };

    fetchData();
    const interval = setInterval(fetchData, 60000); // Fetch every minute

    return () => {
      clearInterval(interval);
    };
  }, []);

  // Handle seat click
  const handleSeatClick = (seat) => {
    setSelectedSpace(seat);
    setShowDialog(true);

    if (seat && seat.employee) {
      setEmployeeDetails(seat.employee);
    } else {
      setEmployeeDetails({
        name: '',
        email: '',
        department: '',
        employeeId: ''
      });
    }
  };

  // Handle room click
  const handleRoomClick = (room) => {
    setSelectedSpace(room);
    setShowDialog(true);
  };

  // Handle assignment
  const handleAssign = async () => {
    try {
      await fetch(`http://localhost:8080/api/seats/${selectedSpace.id}/assign`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          name: employeeDetails.name,
          email: employeeDetails.email,
          department: employeeDetails.department,
          employeeId: employeeDetails.employeeId
        })
      });
      setShowDialog(false);
      // Refresh seats
      const response = await fetch('http://localhost:8080/api/seats');
      const data = await response.json();
      setSeats(data);
      setEmployeeDetails({
        name: '',
        email: '',
        department: '',
        employeeId: ''
      });
    } catch (error) {
      console.error('Error assigning seat:', error);
    }
  };

  // Handle editing seat assignment
  const handleEditAssignment = async () => {
    try {
      await fetch(`http://localhost:8080/api/seats/${selectedSpace.id}/edit`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(employeeDetails)
      });
      setShowDialog(false);
      // Refresh seats
      const response = await fetch('http://localhost:8080/api/seats');
      const data = await response.json();
      setSeats(data);
    } catch (error) {
      console.error('Error editing seat assignment:', error);
    }
  };

  // Handle unassigning seat
  const handleUnassign = async () => {
    try {
      await fetch(`http://localhost:8080/api/seats/${selectedSpace.id}/unassign`, {
        method: 'DELETE'
      });
      setShowDialog(false);
      // Refresh seats
      const response = await fetch('http://localhost:8080/api/seats');
      const data = await response.json();
      setSeats(data);
    } catch (error) {
      console.error('Error unassigning seat:', error);
    }
  };

  // Handle booking
  const handleBook = async () => {
    try {
      await fetch(`http://localhost:8080/api/conference-rooms/${selectedSpace.id}/book`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(bookingDetails)
      });
      setShowDialog(false);
      // Refresh conference rooms
      const response = await fetch('http://localhost:8080/api/conference-rooms');
      const data = await response.json();
      setConferenceRooms(data);
      setBookingDetails({
        employeeName: '',
        department: '',
        date: '',
        startTime: '',
        endTime: ''
      });
    } catch (error) {
      console.error('Error booking room:', error);
    }
  };

  return (
    <div style={{ margin: '20px' }}>
      <h1>Office Layout</h1>
      
      <div style={{ display: 'flex', gap: '20px' }}>
        {/* Seats Section */}
        <div style={{ border: '1px solid black', padding: '20px', flexBasis: '50%' }}>
          <h2>Workstations</h2>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(5, 1fr)', gap: '10px' }}>
            {[...Array(25)].map((_, index) => {
              const seat = seats.find(s => s.id === index + 1);
              return (
                <button
                  key={index}
                  onClick={() => handleSeatClick(seat)}
                  style={{
                    padding: '10px',
                    backgroundColor: seat && seat.occupied ? 'blue' : 'gray',
                    color: 'white'
                  }}
                >
                  {seat ? seat.seatNumber : `S${index + 1}`}
                </button>
              );
            })}
          </div>
        </div>

        {/* Conference Rooms Section */}
        <div style={{ border: '1px solid black', padding: '20px', flexBasis: '50%' }}>
          <h2>Conference Rooms</h2>
          <div>
            {conferenceRooms.map((room) => (
              <button
                key={room.id}
                onClick={() => handleRoomClick(room)}
                style={{
                  display: 'block',
                  width: '100%',
                  padding: '10px',
                  textAlign: 'left',
                  border: '1px solid black',
                  marginBottom: '10px'
                }}
              >
                <div>{room.roomName}</div>
                <div style={{ color: room.occupied ? 'red' : 'green' }}>
                  {room.occupied ? 'Occupied' : 'Available'}
                </div>
              </button>
            ))}
          </div>
        </div>
      </div>

      {/* Dialog */}
      {showDialog && (
        <div style={{
          position: 'fixed',
          top: 0,
          left: 0,
          width: '100%',
          height: '100%',
          backgroundColor: 'rgba(0, 0, 0, 0.5)',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center'
        }}>
          <div style={{ backgroundColor: 'white', padding: '20px', maxWidth: '400px', width: '100%' }}>
            <h3 style={{ marginBottom: '20px' }}>
              {selectedSpace && selectedSpace.roomName
                ? 'Book Conference Room'
                : selectedSpace && selectedSpace.employee
                ? 'Edit Seat Assignment'
                : 'Assign Seat'}
            </h3>
            
            {selectedSpace && selectedSpace.roomName ? (
              <div>
                <input
                  type="text"
                  placeholder="Employee Name"
                  value={bookingDetails.employeeName}
                  onChange={(e) => setBookingDetails({
                    ...bookingDetails,
                    employeeName: e.target.value
                  })}
                />
                <input
                  type="text"
                  placeholder="Department"
                  value={bookingDetails.department}
                  onChange={(e) => setBookingDetails({
                    ...bookingDetails,
                    department: e.target.value
                  })}
                />
                <input
                  type="date"
                  value={bookingDetails.date}
                  onChange={(e) => setBookingDetails({
                    ...bookingDetails,
                    date: e.target.value
                  })}
                />
                <input
                  type="time"
                  value={bookingDetails.startTime}
                  onChange={(e) => setBookingDetails({
                    ...bookingDetails,
                    startTime: e.target.value
                  })}
                />
                <input
                  type="time"
                  value={bookingDetails.endTime}
                  onChange={(e) => setBookingDetails({
                    ...bookingDetails,
                    endTime: e.target.value
                  })}
                />
                <button onClick={handleBook}>
                  Book Room
                </button>
              </div>
            ) : (
              <div>
                {selectedSpace && selectedSpace.employee ? (
                  <div>
                    <input
                      type="text"
                      placeholder="Name"
                      value={employeeDetails.name}
                      onChange={(e) => setEmployeeDetails({
                        ...employeeDetails,
                        name: e.target.value
                      })}
                    />
                    <input
                      type="email"
                      placeholder="Email"
                      value={employeeDetails.email}
                      onChange={(e) => setEmployeeDetails({
                        ...employeeDetails,
                        email: e.target.value
                      })}
                    />
                    <input
                      type="text"
                      placeholder="Department"
                      value={employeeDetails.department}
                      onChange={(e) => setEmployeeDetails({
                        ...employeeDetails,
                        department: e.target.value
                      })}
                    />
                    <input
                      type="text"
                      placeholder="Employee ID"
                      value={employeeDetails.employeeId}
                      onChange={(e) => setEmployeeDetails({
                        ...employeeDetails,
                        employeeId: e.target.value
                      })}
                    />
                    <button onClick={handleEditAssignment}>
                      Save Changes
                    </button>
                    <button onClick={handleUnassign}>
                      Unassign Seat
                    </button>
                  </div>
                ) : (
                  <div>
                    <input
                      type="text"
                      placeholder="Name"
                      value={employeeDetails.name}
                      onChange={(e) => setEmployeeDetails({
                        ...employeeDetails,
                        name: e.target.value
                      })}
                    />
                    <input
                      type="email"
                      placeholder="Email"
                      value={employeeDetails.email}
                      onChange={(e) => setEmployeeDetails({
                        ...employeeDetails,
                        email: e.target.value
                      })}
                    />
                    <input
                      type="text"
                      placeholder="Department"
                      value={employeeDetails.department}
                      onChange={(e) => setEmployeeDetails({
                        ...employeeDetails,
                        department: e.target.value
                      })}
                    />
                    <input
                      type="text"
                      placeholder="Employee ID"
                      value={employeeDetails.employeeId}
                      onChange={(e) => setEmployeeDetails({
                        ...employeeDetails,
                        employeeId: e.target.value
                      })}
                    />
                    <button onClick={handleAssign}>
                      Assign Seat
                    </button>
                  </div>
                )}
              </div>
            )}
            
            <button onClick={() => setShowDialog(false)}>
              Cancel
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default OfficeLayout;
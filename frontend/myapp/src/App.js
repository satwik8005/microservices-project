import React, { useState } from 'react';
import axios from 'axios';
import SeatDashboard from './components/SeatDashboard';
import './App.css';

function App() {
 const [view, setView] = useState('register');
 const [user, setUser] = useState(null);
 const [loginData, setLoginData] = useState({ username: '', password: '' });
 const [registerData, setRegisterData] = useState({
   id: '',
   userName: '',
   name: '',
   role: 'ROLE_DEVELOPER', 
   password: '',
   projectName: '',
   active: true
 });
 

 const handleRegister = async (e) => {
   e.preventDefault();
   try {
     await axios.post('http://localhost:8080/auth-service/auth/register', registerData);
     setView('login');
   } catch (error) {
     console.error('Registration error:', error);
   }
 };

 const handleLogin = async (e) => {
  e.preventDefault();
  try {
    const response = await axios.post('http://localhost:8080/auth-service/auth/authenticate', loginData);
    if (response.status === 200) {
      const credentials = btoa(`${loginData.username}:${loginData.password}`);
      localStorage.setItem('credentials', credentials);
      
      const userResponse = await axios.get(
        `http://localhost:8080/auth-service/auth/getEmployeeByUsername/${loginData.username}`,
        { headers: { Authorization: `Basic ${credentials}` }}
      );
      setUser(userResponse.data);
    }
  } catch (error) {
    console.error('Login error:', error);
  }
};

 return (
   <div className="app">
     {!user ? (
       <div className="auth-container">
         {view === 'register' ? (
           <div className="form-container">
             <h2>Register</h2>
             <form onSubmit={handleRegister}>
               <input type="number" placeholder="Employee ID" 
                 onChange={e => setRegisterData({...registerData, id: e.target.value})} />
               <input type="text" placeholder="Username"
                 onChange={e => setRegisterData({...registerData, userName: e.target.value})} />
               <input type="text" placeholder="Full Name"
                 onChange={e => setRegisterData({...registerData, name: e.target.value})} />
               <select onChange={e => setRegisterData({...registerData, role: e.target.value})}>
                 <option value="ROLE_DEVELOPER">Developer</option>
                 <option value="ROLE_MANAGER">Manager</option>
                 <option value="ROLE_ADMIN">Admin</option>
               </select>
               <input type="password" placeholder="Password"
                 onChange={e => setRegisterData({...registerData, password: e.target.value})} />
               <input type="text" placeholder="Project Name"
                 onChange={e => setRegisterData({...registerData, projectName: e.target.value})} />
               <button type="submit">Register</button>
             </form>
             <button onClick={() => setView('login')}>Already have an account? Login</button>
           </div>
         ) : (
           <div className="form-container">
             <h2>Login</h2>
             <form onSubmit={handleLogin}>
               <input type="text" placeholder="Username"
                 onChange={e => setLoginData({...loginData, username: e.target.value})} />
               <input type="password" placeholder="Password"
                 onChange={e => setLoginData({...loginData, password: e.target.value})} />
               <button type="submit">Login</button>
             </form>
             <button onClick={() => setView('register')}>Need an account? Register</button>
           </div>
         )}
       </div>
     ) : (
       <SeatDashboard user={user} />
     )}
   </div>
 );
}

export default App;
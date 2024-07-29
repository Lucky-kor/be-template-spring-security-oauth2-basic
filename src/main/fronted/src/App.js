import React, { useState, useEffect } from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import axios from 'axios';
import './main.css';
import Qna from './Qna';
import Write from './Write';
import QnaList from './QnaList';
import { Link } from 'react-router-dom';

const App = () => {
  const [accessToken, setAccessToken] = useState('');
  const [refreshToken, setRefreshToken] = useState('');

  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search);
    const accessToken = urlParams.get('access_token');
    const refreshToken = urlParams.get('refresh_token');

    localStorage.setItem('accessToken', accessToken);
    localStorage.setItem('refreshToken', refreshToken);

    setAccessToken(accessToken);
    setRefreshToken(refreshToken);

    document.cookie = `refreshtoken=${refreshToken}`;
  }, []);

  const Router = () => {
    return (
      <BrowserRouter>
          <Routes>
            <Route path="/" element={<QnaList />} />
            <Route path='/qna/:qnaId' element ={<Qna/>} />
            <Route path="/write" element={<Write />} />
          </Routes>
      </BrowserRouter>
    );
  };
  
  return (
    <div>
      <div>
        <Router>
        </Router>
      </div>
      <div>
        <p>
          <span>Access Token: </span><span style={{ color: 'blue', fontSize: '5pt' }}>{accessToken}</span>
        </p>
        <p>
          <span>Refresh Token: </span><span style={{ color: 'blue', fontSize: '5pt' }}>{refreshToken}</span>
        </p>
      </div>
      
    </div>
  );
};

export default App;
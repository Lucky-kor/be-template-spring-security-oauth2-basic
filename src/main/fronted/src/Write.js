import axios from 'axios';
import React, { useState } from 'react';
import { useNavigate, useParams } from "react-router-dom";

const Write = () => {
  const [qnaTitle, setQnaTitle] = useState('');
  const [qnaBody, setQnaBody] = useState('');
  const [isLock, setIsLock] = useState(false);

  let accessToken = window.localStorage.getItem('accessToken');

  const navigate = useNavigate();
  const isModify = useParams().isModify;

  const handlePostQna = async () => {
    try {
      const response = await axios.post(
        'http://127.0.0.1:8080/v11/board/qna',
        {
          title: qnaTitle,
          body: qnaBody,
          lock: isLock ? 1 : 0
        },
        {
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${accessToken}`,
          },
        }
      );
      alert('질문 남기기 성공!');
      if(response !== undefined)
        navigate('/');
    } catch (error) {
      alert(JSON.stringify(error.message));
    }
  };

  return (<div>
        <div>
            <label htmlFor="qnaTitle">제목</label>
            <input className='qnatitleinput' type="text" value={qnaTitle} onChange={(e) => setQnaTitle(e.target.value)} />
            <input type="checkbox" id="isLock" checked={isLock} onChange={() => setIsLock(!isLock)} />
            <label htmlFor="isLock">🔒</label>
        </div>
        <div>
            <label htmlFor="qnaBody">내용</label>
            <input className='qnabodyinput' type="text" value={qnaBody} onChange={(e) => setQnaBody(e.target.value)} />
        </div>
        <div>
            <button className="btn-1 custom-btn" onClick={handlePostQna}>작성</button>
        </div>
  </div>);
};

export default Write;

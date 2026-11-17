import React from 'react'


const KEYBOARD_ROWS = [
  ['Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P'],
  ['A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L'],
  ['ENTER', 'Z', 'X', 'C', 'V', 'B', 'N', 'M', 'BACKSPACE']
];
function KeyBoardComp() {
  return (
    <div className='container mt-4'>
  {KEYBOARD_ROWS.map((row , rowIndex)=>
(
    <div key={rowIndex}
        className="d-flex justify-content-center gap-2 mb-2"
    >
        {
     row.map((key)=>(
        <button key={key}
        type = "button"
        className='btn btn-outline-dark fw-bold'
          
        >{key}</button>

     ))
 } </div>
))
}
</div>);
}

export default KeyBoardComp;
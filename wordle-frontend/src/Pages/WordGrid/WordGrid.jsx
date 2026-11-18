import React,{ useState,useRef } from 'react'

function WordGrid() {

  const[grid,setGrid]= useState(Array(30).fill(''));
  const newRefs= useRef([]);
   const handleCharChange=(e,index)=>
   {
      const newVal=e.target.value;

      const newArr = [...grid];
      newArr[index]=newVal.slice(-1).toUpperCase();
      setGrid(newArr);
       newRefs.current[index+1]?.focus();
       console.log(newRefs)
   }
  return (
    <div className='container d-flex justify-content-center mt-4'>
        <div className='d-grid gap-2'
        style={{gridTemplateColumns :'repeat(5,55px)'}}>


        {
       
            grid.map((length,index)=>
            (
                <input
                key={index}
                value={length}
                ref={(el)=>{newRefs.current[index]=el}}
                className='border border-dark text-center fw-bold'
                onChange={(e) => handleCharChange(e,index)}
                style ={{height:'55px' }}
                ></input>
            ))
        }
                </div>
    </div>

  );
}

export default WordGrid
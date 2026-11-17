import React from 'react'

function WordGrid() {
  return (
    <div className='container d-flex justify-content-center mt-4'>
        <div className='d-grid gap-2'
        style={{gridTemplateColumns :'repeat(5,55px)'}}>


        {
            Array.from({length : 5 *6}).
            map((_,index)=>
            (
                <div
                key={index}
                className='border border-dark'
                style ={{height:'55px' }}
                ></div>
            ))
        }
                </div>
    </div>

  );
}

export default WordGrid
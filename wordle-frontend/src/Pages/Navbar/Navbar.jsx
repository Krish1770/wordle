import React from 'react'

function Navbar() {
  return (
    <nav className='navbar navbar-expand-lg bg-dark navbar-dark'>
        <div className='container'>
            <h5 className='navbar-brand mx-auto my-auto '>WORDLE</h5>
          <div>
            <ul className='navbar-nav flex-row gap-3 ms-auto'>
                  <li className='nav-item'>
                    <a className='nav-link active'>Play</a>
                </li>
                <li className='nav-item'>
                    <a className='nav-link '>Stats</a>
                </li>
                 <li className='nav-item'>
                    <a className='nav-link '>Settings</a>
                </li>
            </ul>
          </div>
        </div>

    </nav>
  )
}

export default Navbar
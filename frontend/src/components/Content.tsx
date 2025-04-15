import React from 'react'
import {WithChildren} from "./core/WithChildren";


const Content: React.FC<WithChildren> = ({children}) => {
    return (
        <div className='content flex-row-fluid m-5' id='zero_content'>
            {children}
        </div>
    )
}

export {Content}

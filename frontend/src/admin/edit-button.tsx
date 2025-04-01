import {Link} from "react-router-dom";
import {ZeroIcon} from "../components/ZeroIcon";

export const EditButton = ({type, id}: {type: string, id: string}) => (
    <Link className="btn btn-secondary btn-icon btn-sm" to={`/${type}/${id}`} css={{
        textDecoration: 'none',
        whiteSpace: 'nowrap',
    }}>
        <ZeroIcon iconType={'solid'} iconName={'pencil'} className='fs-4 me-1 d-inline-block'/>
    </Link>
)

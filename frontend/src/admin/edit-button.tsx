import {Link} from "react-router";

export const EditButton = ({surveyId}: {surveyId: string}) => (
    <Link className="p-button p-button-success" to={`/bedrijven-uitvraag/${surveyId}`} css={{
        textDecoration: 'none',
        whiteSpace: 'nowrap',
    }}>
        <i className="pi pi-pencil"></i>
    </Link>
)

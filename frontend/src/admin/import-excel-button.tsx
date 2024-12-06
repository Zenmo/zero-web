import {FunctionComponent} from "react"
import {useNavigate} from "react-router"
import {Button} from "primereact/button"
import { SiMicrosoftexcel } from "react-icons/si";

export const ImportExcelButton: FunctionComponent = () => {
    const navigate = useNavigate()

    return (
        <Button onClick={() => navigate("/admin/import-excel")} aria-label="Importeer Excel">
            <SiMicrosoftexcel />
            &nbsp;
            Importeer Excel
        </Button>
    )
}

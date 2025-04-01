import {FunctionComponent} from "react"
import {Link} from "react-router-dom"
import {Content} from "./Content";

type LinkProps = {
    to: string
    title: string
}

const linksData: LinkProps[] = [
    {to: "/bedrijven-hessenpoort", title: "Uitvraag Hessenpoort invullen"},
    {to: "/bedrijven-de-wieken", title: "Uitvraag De Wieken invullen"},
    {to: "/admin", title: "Uitvragen beheren"},
    {to: "/proof-of-concept", title: "Oud proof of concept \"simuleer je buurt\""},
    {to: "https://zenmo.com", title: "Zenmo hoofdpagina"},
    {to: "https://keycloak.zenmo.com/realms/zenmo/account/", title: "Eigen gebruikersaccount beheren"},
    {to: "https://keycloak.zenmo.com/admin/zenmo/console/", title: "Alle gebruikersaccounts beheren"},
    {to: "https://github.com/zenmo/zero", title: "Broncode van deze website"},
]


export const Intro: FunctionComponent = () => (
    <Content>
        <div className='d-flex flex-column flex-lg-row flex-column-fluid'>
            <div className='d-flex flex-column flex-lg-row-fluid w-lg-70 p-10'>
                <div className='d-flex flex-center flex-column flex-lg-row-fluid'>
                    <div className='w-lg-700px'>
                        <p className={'fs-1 fw-bold mb-10'}>Welkom bij Zenmo Zero</p>
                        <div className={'d-flex flex-column flex-grow-1 align-content-center'}>
                            <p className={'fs-3'}>Deze pagina is in aanbouw. Wat u kunt vinden op deze website:</p>
                            {
                                linksData
                                    .slice(0, 4)
                                    .map((link, index) => (
                                        <li className="d-flex align-items-center py-2">
                                            <Link key={index} to={link.to} className="mb-2">
                                                <span className="bullet bullet-dot bg-dark me-5"></span>{link.title}
                                            </Link>
                                        </li>
                                    ))
                            }
                        </div>
                    </div>
                </div>
            </div>
            <div className='d-flex flex-lg-row-fluid mw-lg-30 flex-center'>
                <div className='d-flex flex-lg-row-fluid flex-center'>
                    <div className={'d-flex flex-column flex-grow-1 align-content-center'}>
                        <span className={'fs-4 fw-medium text-primary'}>Links naar andere plekken</span>
                        {
                            linksData
                                .slice(4)
                                .map((link, index) => (
                                    <li className="d-flex align-items-center py-2">
                                        <span className="bullet bullet-dot bg-dark me-5"></span>
                                        <a href={link.to}>{link.title}</a>
                                    </li>
                                ))
                        }
                    </div>
                </div>
            </div>
        </div>
    </Content>
)
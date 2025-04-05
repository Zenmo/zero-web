import {FunctionComponent} from "react"
import {Link} from "react-router-dom"
import {Content} from "./Content";
import {BsDot} from "react-icons/bs";

type LinkProps = {
    to: string
    title: string
}

const linksData: LinkProps[] = [
    // {to: "/bedrijven-hessenpoort", title: "Uitvraag Hessenpoort invullen"},
    // {to: "/bedrijven-de-wieken", title: "Uitvraag De Wieken invullen"},
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
            <div className='d-flex flex-column flex-lg-row-fluid p-5'>
                <div className='d-flex justify-content-center flex-column flex-lg-row-fluid'>
                    <div style={{width: '700px'}}>
                        <p className={'fs-1 fw-bold mb-5'}>Welkom bij Zenmo Zero</p>
                        <div className={'d-flex flex-column flex-grow-1 align-content-center'}>
                            <p className={'fs-5'}>Deze pagina is in aanbouw. Wat u kunt vinden op deze website:</p>
                            {
                                linksData
                                    .slice(0, 2)
                                    .map((link, index) => (
                                        <li className="d-flex align-items-center mb-2">
                                            <Link key={index} to={link.to} className="text-decoration-none">
                                                <BsDot className={"me-1 text-dark"}/>
                                                {link.title}
                                            </Link>
                                        </li>
                                    ))
                            }
                        </div>
                    </div>
                </div>
            </div>
            <div className='d-flex flex-lg-row-fluid'>
                <div className='d-flex flex-lg-row-fluid '>
                    <div className={'d-flex flex-column flex-grow-1 align-content-center justify-content-center'}>
                        <span className={'fs-5 mb-2'}>Links naar andere plekken</span>
                        {
                            linksData
                                .slice(2)
                                .map((link, index) => (
                                    <li className="d-flex align-items-center mb-2">
                                        <BsDot className={"me-1 text-dark"}/>
                                        <a className={'text-decoration-none'} href={link.to}>{link.title}</a>
                                    </li>
                                ))
                        }
                    </div>
                </div>
            </div>
        </div>
    </Content>
)
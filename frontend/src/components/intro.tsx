import {FunctionComponent} from "react"
import {Link} from "react-router-dom"
import {Content} from "./Content"
import {BsDot} from "react-icons/bs"
import {linksToOtherPlaces, pageLinksData} from "./core/data"


export const Intro: FunctionComponent = () => (
    <Content>
        <div className="d-flex flex-column flex-lg-row flex-column-fluid justify-content-center">
            <div className="d-flex flex-column flex-lg-row-fluid p-5">
                <div className="d-flex justify-content-center flex-column flex-lg-row-fluid">
                    <p className={"fs-1 fw-bold mb-5"}>Welkom bij Zenmo Zero</p>
                    <div className={"d-flex flex-column flex-grow-1 align-content-center"}>
                        <p className={"fs-5"}>Deze pagina is in aanbouw. Wat u kunt vinden op deze website:</p>
                        {
                            pageLinksData
                                .map((link, index) => (
                                    <li className="d-flex align-items-center mb-2">
                                        <Link key={index} to={link.to} className="text-decoration-none">
                                            <BsDot className={"me-1 text-dark"} />
                                            {link.title}
                                        </Link>
                                    </li>
                                ))
                        }
                    </div>
                </div>
            </div>
            <div className="d-flex flex-lg-row-fluid justify-content-center p-5">
                <div className={"d-flex flex-column justify-content-center"}>
                    <span className={"fs-5 mb-2"}>Links naar andere plekken</span>
                    {
                        linksToOtherPlaces
                            .map((link, index) => (
                                <li className="d-flex align-items-center mb-2">
                                    <BsDot className={"me-1 text-dark"} />
                                    <a className={"text-decoration-none"} href={link.to}>{link.title}</a>
                                </li>
                            ))
                    }
                </div>
            </div>
        </div>
    </Content>
)
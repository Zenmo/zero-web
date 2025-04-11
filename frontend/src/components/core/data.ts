import {LinkProps} from "./_models"

const pageLinksData: LinkProps[] = [
    {to: "/admin", title: "Uitvragen beheren"},
    {to: "/proof-of-concept", title: "Oud proof of concept \"simuleer je buurt\""},
]

const linksToOtherPlaces: LinkProps[] = [
    {to: "https://zenmo.com", title: "Zenmo hoofdpagina"},
    {to: "https://keycloak.zenmo.com/realms/zenmo/account/", title: "Eigen gebruikersaccount beheren"},
    {to: "https://keycloak.zenmo.com/admin/zenmo/console/", title: "Alle gebruikersaccounts beheren"},
    {to: "https://github.com/zenmo/zero", title: "Broncode van deze website"},
]


export {pageLinksData, linksToOtherPlaces}

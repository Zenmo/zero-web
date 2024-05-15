
export type ProjectConfiguration = {
    name: ProjectName,
    email: string,
    logo?: string,
    authorizationPdf?: string,
}

export type ProjectName = 'Hessenpoort' | 'De Wieken'

export const HESSENPOORT: ProjectConfiguration = {
    name: 'Hessenpoort',
    email: 'info@ondernemersvereniginghessenpoort.nl',
    logo: '/logo-hessenpoort.png',
    authorizationPdf: '/spectral-machtiging.pdf',
}

export const DE_WIEKEN: ProjectConfiguration = {
    name: 'De Wieken',
    email: 'info@zenmo.com',
    authorizationPdf: '/machtiging-datadeling-de-wieken.pdf'
}

export const getProjectConfiguration = (projectName: ProjectName): ProjectConfiguration => {
    switch (projectName) {
        case 'Hessenpoort':
            return HESSENPOORT
        case 'De Wieken':
            return DE_WIEKEN
        default:
            throw new Error(`Unknown project name: ${projectName}`)
    }
}

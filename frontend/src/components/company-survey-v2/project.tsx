import {fetchBuurtcodesByProject} from "../../panden-select/fetch-buurtcodes"
import {ztorFetch} from "../../services/ztor-fetch";
import {Project} from "zero-zummon"

export type FrontendProjectConfiguration = {
    name: ProjectName,
    email: string,
    logo?: string,
    authorizationPdf?: string,
    showTractors: boolean,
}

export type ProjectConfiguration = FrontendProjectConfiguration & {
    buurtcodes: readonly string[],
}

export type ProjectName = 'Hessenpoort' | 'De Wieken' | "Energiepolder Hoeksche Waard LTO"

export const castProjectName: (projectName: string) => ProjectName = projectName => projectName as ProjectName

export const HESSENPOORT = {
    name: 'Hessenpoort',
    email: 'info@ondernemersvereniginghessenpoort.nl',
    logo: '/logo-hessenpoort.png',
    authorizationPdf: '/spectral-machtiging.pdf',
} satisfies Partial<FrontendProjectConfiguration>

export const DE_WIEKEN = {
    name: 'De Wieken',
    email: 'info@zenmo.com',
    authorizationPdf: '/machtiging-datadeling-de-wieken.pdf',
} satisfies Partial<FrontendProjectConfiguration>

const GENERIC: FrontendProjectConfiguration = {
    name: castProjectName('Placeholder project'),
    email: 'info@zenmo.com',
    authorizationPdf: '/placeholer-machtiging.pdf',
    showTractors: false,
}

const configs: Record<ProjectName, Partial<FrontendProjectConfiguration>> = {
    'Hessenpoort': HESSENPOORT,
    'De Wieken': DE_WIEKEN,
    'Energiepolder Hoeksche Waard LTO': {
        showTractors: true,
    }
}

export async function getProjectConfiguration(projectName: ProjectName): Promise<ProjectConfiguration> {
    const buurtcodes = await fetchBuurtcodesByProject(projectName)

    return {
        ...GENERIC,
        name: projectName,
        ...(configs[projectName] || {}),
        buurtcodes,
    }
}

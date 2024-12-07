import {useState} from "react";
import {useOnce} from "../hooks/use-once";
import {Project, projectsFromJson} from "zero-zummon"

type UseProjectReturn = {
    loading: boolean,
    projects: Project[],
    changeProject: (newProject: Project) => void,
    removeProject: (projectId: string) => void,
}

export const useProjects = (): UseProjectReturn => {
    const [loading, setLoading] = useState(true)
    const [projects, setProjects] = useState<Project[]>([])

    const changeProject = (newProject: Project) => {
        setProjects(projects.map(project => project.id.toString() === newProject.id.toString() ? newProject : project))
    }

    useOnce(async () => {
        try {
            const response = await fetch(import.meta.env.VITE_ZTOR_URL + '/projects', {
                credentials: 'include',
            })
            if (response.status === 401) {
                redirectToLogin()
                return
            }

            setProjects(projectsFromJson(await response.text()))
        } catch (error) {
            alert((error as Error).message)
        } finally {
            setLoading(false)
        }
    })

    const removeProject = (projectId: any) => {
        setProjects(projects.filter(project => project.id.toString() !== projectId.toString()))
    }

    return {
        loading,
        projects,
        changeProject,
        removeProject,
    }
}

export const redirectToLogin = () => {
    window.location.href = import.meta.env.VITE_ZTOR_URL + '/login?redirectUrl=' + encodeURIComponent(window.location.href)
}

import {css} from '@emotion/react'
import {Tabs, TabsProps} from "antd";
import {ProjectName} from './project'
import {SurveyTab} from './survey-tab'
import {Transport} from "./transport";
import {useFieldArray, UseFormReturn} from "react-hook-form";
import {GridConnection} from "./grid-connection";
import {PlusOutlined} from "@ant-design/icons";
import React, {FunctionComponent} from 'react'

export const SurveyTabs: FunctionComponent<{form: UseFormReturn, project: ProjectName }> = ({form, project}) => {
    const {register, control} = form

    const { fields, append, prepend, remove, swap, move, insert } = useFieldArray({
        control,
        name: 'tabs',
    })

    const onEdit = (
        targetKey: React.MouseEvent | React.KeyboardEvent | string,
        action: 'add' | 'remove',
    ) => {
        if (action === 'add') {
            append({
                address: {},
                gridConnection: {},
            });
        } else {
            // @ts-ignore
            remove(parseInt(targetKey));
        }
    };

    const items: TabsProps['items'] = fields.map((field, index) => {
        return {
            key: index.toString(),
            label: `${index + 1}e netaansluiting`,
            children: <SurveyTab form={form} prefix={`tabs.${index}`} isFirst={index === 0} project={project} />,
            closable: index > 0,
        }
    })

    return (
        <Tabs type="editable-card" items={items} addIcon={<><PlusOutlined /> extra aansluiting</>} onEdit={onEdit}/>
    )
}
import {PlusOutlined} from '@ant-design/icons'
import {Tabs, TabsProps} from 'antd'
import React, {FunctionComponent} from 'react'
import {useFieldArray, UseFormReturn} from 'react-hook-form'
import {ProjectConfiguration} from './project'
import {emptyGridConnection} from './survey'
import {SurveyTab} from './survey-tab'

export const SurveyTabs: FunctionComponent<{form: UseFormReturn, project: ProjectConfiguration }> = ({form, project}) => {
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
                gridConnection: emptyGridConnection,
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
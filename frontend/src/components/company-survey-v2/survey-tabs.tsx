import {Tabs, TabsProps} from "antd";
import {Transport} from "./transport";
import {useFieldArray, UseFormReturn} from "react-hook-form";
import {GridConnection} from "./grid-connection";
import {PlusOutlined} from "@ant-design/icons";
import React from "react";

export const SurveyTabs = ({form}: {form: UseFormReturn}) => {
    const {register, control} = form
    const name = "gridConnections"

    const { fields, append, prepend, remove, swap, move, insert } = useFieldArray({
        control,
        name,
    })

    const onEdit = (
        targetKey: React.MouseEvent | React.KeyboardEvent | string,
        action: 'add' | 'remove',
    ) => {
        if (action === 'add') {
            append({});
        } else {
            // @ts-ignore
            remove(parseInt(targetKey));
        }
    };

    const items: TabsProps['items'] = [
        {
            key: 'transport',
            label: 'Mobiliteit',
            children: <Transport form={form} prefix="transport" />,
        },
        ...fields.map((item, index) => {
            return {
                key: index.toString(),
                label: fields.length == 1 ? `Netaansluiting` : `${index + 1}e netaansluiting`,
                children: <GridConnection key={index} form={form} prefix={`${name}.${index}`}/>,
                closable: index > 0,
            }
        })
    ];

    return (
        <Tabs type="editable-card" items={items} addIcon={<><PlusOutlined /> extra aansluiting</>} onEdit={onEdit}/>
    )
}
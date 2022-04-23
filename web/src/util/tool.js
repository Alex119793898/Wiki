export function array2Tree(arr,parentId){
    const result = [];
    arr.forEach(item=>{
        if(Number(item.parent) === Number(parentId)){
            result.push(item);

            const children = array2Tree(arr,item.id);
            if(children.length>0){
                item.children = children;
            }
        }
    })

    return result;
}

export function setDisabled(arr,id){
    arr.forEach(node=>{
        if(Number(node.id) === Number(id)){
            node.disabled = true;

            const children = node.children;
            if(children && children.length>0){
                children.forEach(child=>{
                    setDisabled(children,child.id)
                })
            }
        }else{
            const children = node.children;
            if(children && children.length>0){
                setDisabled(children,id);
            }
        }
    })
}

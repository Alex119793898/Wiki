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

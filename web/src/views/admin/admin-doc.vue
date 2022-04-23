<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <p>
        <a-form layout="inline" :model="queryForm">
          <!--<a-form-item>
            <a-input v-model:value="queryForm.name" placeholder="名称"/>
          </a-form-item>-->
          <a-form-item>
            <a-button type="primary" html-type="submit" @click="handleQuery()">查询</a-button>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="add()">新增</a-button>
          </a-form-item>
        </a-form>

      </p>
      <a-table
          :columns="columns"
          :data-source="levelTree"
          :pagination="false"
          :loading="tableLoading"
      >
        <template #bodyCell="{column,record}">
          <template v-if="column.key == 'cover'">
            <img :src="record.cover" alt="avater">
          </template>
          <template v-else-if="column.key == 'action'">
            <a-space size="small">
              <a-button type="primary" @click="edit(record)">编辑</a-button>
              <a-popconfirm
                  title="删除后不可恢复，确定删除吗?"
                  ok-text="Yes"
                  cancel-text="No"
                  @confirm="del(record.id)"
              >
                <a-button type="danger">删除</a-button>
              </a-popconfirm>
            </a-space>

          </template>
        </template>
        <!--<template v-slot:cover="{record}">
          <img :src="record.cover" alt="avater">
        </template>-->
      </a-table>
    </a-layout-content>
  </a-layout>

  <div>
    <a-modal
        title="文档表单"
        v-model:visible="modalVisible"
        :confirm-loading="modalLoading"
        @ok="handleOk"
    >
      <a-form :model="doc" :label-col="{span:6}">
        <a-form-item label="名称">
          <a-input v-model:value="doc.name" />
        </a-form-item>
        <a-form-item label="父文档">
          <!--<a-select v-model:value="doc.parent">
            <a-select-option value="0" >
              无
            </a-select-option>
            <a-select-option
                v-for="c in levelTree"
                :key="c.id"
                :value="c.id"
                :disabled="doc.id === c.id"
            >
              {{c.name}}
            </a-select-option>
          </a-select>-->
          <a-tree-select
              v-model:value="doc.parent"
              :tree-data="levelTreeSelect"
              :fieldNames="{label:'name', key:'id', value: 'id'}"
              show-search
              :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
              placeholder="Please select"
              tree-default-expand-all
          >
          </a-tree-select>
        </a-form-item>
        <a-form-item label="顺序">
          <a-input v-model:value="doc.sort" />
        </a-form-item>

      </a-form>
    </a-modal>
  </div>
</template>
<script>
import { defineComponent, onMounted, ref, reactive, nextTick } from 'vue';
import axios from "axios";
import { message } from 'ant-design-vue';
import { array2Tree, setDisabled } from '@/util/tool'

const columns = [
  {
    title: '名称',
    dataIndex: 'name',
    key: 'age',
  },
  {
    title: '父文档',
    dataIndex: 'parent',
  },
  {
    title: '顺序',
    dataIndex: 'sort',
  },
  {
    title: '操作',
    key: 'action',
  },
];

export default defineComponent({
  name:'AdminCategory',
  components: {
  },
  setup() {

    const docs = ref()
    const queryForm = ref({
      name:null
    });
    
    const tableLoading = ref(false);


    /*
    * 树型数据
    * [
        {
          children: [
            {
              id:"",
              name:"VUE"
              parent: 100
              sort: 100
            }
          ]
          id: "100"
          name: "前端开发"
          parent: 0
          sort: 100
        }
    * ]
    * */
    const levelTree = ref([]);
    const levelTreeSelect = ref([]);

    /*请求*/
    const handleQuery = () =>{
      tableLoading.value = true;
      axios.get("/doc/all").then(res=>{
        tableLoading.value = false;
        const data = res.data;
        if(data.success){
          levelTree.value = array2Tree(data.content,0);

          levelTree.value.forEach(ele=>{          //key属性是table表格用于区分的，避免一个展开全都被展开了
            ele.key = ele.id;
          })

        }else{
          message.warning(data.message)
        }
      })
    }

    /*弹窗*/
    const modalText = ref('Content of the modal');
    const modalVisible = ref(false);
    const modalLoading = ref(false);

    const handleOk = () => {
      modalLoading.value = true;
      axios.post("/doc/save",doc.value).then(res=>{
        modalLoading.value = false;

        if(res.data.success){
          modalVisible.value = false;

          handleQuery();
        }else{

          message.warning(res.data.message);
        }

      })
    }

    /*表单*/
    const doc = ref({
      data:{
        cover:'',
        name: '',
        parent: '',
      }
    });

    const edit = record => {
      levelTreeSelect.value = JSON.parse(JSON.stringify(levelTree.value));
      setDisabled(levelTreeSelect.value,record.id);
      console.log(levelTreeSelect.value)
      //levelTreeSelect.value.unshift({id:0,name:'无'});

      doc.value = JSON.parse(JSON.stringify(record));
      modalVisible.value = true;

    }

    const add = ()=>{
      doc.value = {};
      modalVisible.value = true;
    }

    const del = id =>{
      axios.delete("/doc/delete/" + id).then(res=>{
        if(res.data.success){
          handleQuery();
        }
      })
    }

    onMounted(()=>{
      handleQuery();
    })
    return {
      edit,
      add,
      del,
      doc,

      handleQuery,

      handleOk,
      modalText,
      modalVisible,
      modalLoading,
      tableLoading,
      queryForm,
      levelTree,
      levelTreeSelect,
      columns,
    };
  },
});
</script>
<style scoped>
img {
  width: 50px;
  height: 50px;
}
</style>

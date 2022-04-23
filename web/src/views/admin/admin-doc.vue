<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-row :gutter="24">
        <a-col :span="8">
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
              v-if="levelTree.length"
              :columns="columns"
              :data-source="levelTree"
              :pagination="false"
              :loading="tableLoading"
              :defaultExpandAllRows="true"
          >
            <template #bodyCell="{column,record}">
              <template v-if="column.key == 'action'">
                <a-space size="small">
                  <a-button type="primary" @click="edit(record)" size="small">编辑</a-button>
                  <a-popconfirm
                      title="删除后不可恢复，确定删除吗?"
                      ok-text="Yes"
                      cancel-text="No"
                      @confirm="del(record.id)"
                  >
                    <a-button type="danger" size="small">删除</a-button>
                  </a-popconfirm>
                </a-space>

              </template>
            </template>
            <!--<template v-slot:cover="{record}">
              <img :src="record.cover" alt="avater">
            </template>-->
          </a-table>
        </a-col>
        <a-col :span="16">
          <p>
            <a-button type="primary" @click="handleOk">保存</a-button>
          </p>

          <a-form :model="doc" layout="vertical" :label-col="{span:6}">
            <a-form-item>
              <a-input v-model:value="doc.name" placeholder="名称"/>
            </a-form-item>
            <a-form-item>
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
                  placeholder="请选择父文档"
                  tree-default-expand-all
              >
              </a-tree-select>
            </a-form-item>
            <a-form-item>
              <a-input v-model:value="doc.sort" placeholder="顺序"/>
            </a-form-item>
            <a-form-item>
              <div style="border: 1px solid #ccc">
                <Toolbar
                    style="border-bottom: 1px solid #ccc"
                    :editor="editorRef"
                    :defaultConfig="toolbarConfig"
                    :mode="mode"
                />
                <Editor
                    style="height: 500px; overflow-y: hidden;"
                    v-model="doc.content"
                    :defaultConfig="editorConfig"
                    :mode="mode"
                    @onCreated="handleCreated"
                />
              </div>
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>

    </a-layout-content>
  </a-layout>

  <!--<div>
    <a-modal
        title="文档表单"
        v-model:visible="modalVisible"
        :confirm-loading="modalLoading"
        @ok="handleOk"
    >

    </a-modal>
  </div>-->
</template>
<script>
import { defineComponent, onMounted, onBeforeUnmount, ref, shallowRef, reactive, nextTick } from 'vue';
import axios from "axios";
import { message } from 'ant-design-vue';
import { array2Tree, setDisabled } from '@/util/tool'
import {useRoute} from "vue-router";

import '@wangeditor/editor/dist/css/style.css' // 引入 css
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'

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
  components: { Editor, Toolbar },
  setup() {
    const route = useRoute();
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

          //初始化父文档树形下拉选择数据
          levelTreeSelect.value = JSON.parse(JSON.stringify(levelTree.value));
          levelTreeSelect.value.unshift({id:0,name:'无'});

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
        content:'',
      }
    });

    const edit = record => {
      levelTreeSelect.value = JSON.parse(JSON.stringify(levelTree.value));
      setDisabled(levelTreeSelect.value,record.id);
      levelTreeSelect.value.unshift({id:0,name:'无'});

      doc.value = JSON.parse(JSON.stringify(record));
      modalVisible.value = true;

    }

    const add = ()=>{
      doc.value = {
        ebookId: route.query.ebookId
      };

      modalVisible.value = true;
    }

    const del = id =>{
      axios.delete("/doc/delete/" + id).then(res=>{
        if(res.data.success){
          handleQuery();
        }
      })
    }

    // 编辑器实例，必须用 shallowRef
    const editorRef = shallowRef()


    const toolbarConfig = {}
    const editorConfig = { placeholder: '请输入内容...' }

    const handleCreated = (editor) => {
      editorRef.value = editor // 记录 editor 实例，重要！
    }

    onMounted(()=>{
      handleQuery();
    })

    // 组件销毁时，也及时销毁编辑器
    onBeforeUnmount(() => {
      const editor = editorRef.value
      if (editor == null) return
      editor.destroy()
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

      editorRef,
      mode: 'default',
      toolbarConfig,
      editorConfig,
      handleCreated
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

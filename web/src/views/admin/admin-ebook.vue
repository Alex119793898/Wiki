<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <p>
        <a-form layout="inline" :model="queryForm">
          <a-form-item>
            <a-input v-model:value="queryForm.name" placeholder="名称"/>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" html-type="submit" @click="handleQuery({page:1,size:pagination.pageSize})">查询</a-button>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="add()">新增</a-button>
          </a-form-item>
        </a-form>

      </p>
      <a-table
          :columns="columns"
          :data-source="ebooks"
          :pagination="pagination"
          :loading="tableLoading"
          @change="pageChange"
      >
        <template #bodyCell="{column,record}">
          <template v-if="column.key == 'cover'">
            <img :src="record.cover" alt="avater">
          </template>
          <template v-if="column.key == 'category'">
            <span>{{getCategoryName(record.category1Id)}} / {{getCategoryName(record.category2Id)}}</span>
          </template>
          <template v-else-if="column.key == 'action'">
            <a-space size="small">
              <router-link :to="'/admin/doc?ebookId=' + record.id">
                <a-button type="primary" >文档管理</a-button>
              </router-link>
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
        title="电子书表单"
        v-model:visible="modalVisible"
        :confirm-loading="modalLoading"
        @ok="handleOk"
    >
      <a-form :model="ebook" :label-col="{span:6}">
        <a-form-item label="封面">
          <a-input v-model:value="ebook.cover" />
        </a-form-item>
        <a-form-item label="名称">
          <a-input v-model:value="ebook.name" />
        </a-form-item>
        <!--<a-form-item label="分类一">
          <a-input v-model:value="ebook.category1Id" />
        </a-form-item>
        <a-form-item label="分类二">
          <a-input v-model:value="ebook.category2Id" />
        </a-form-item>-->
        <a-form-item label="分类">
          <a-cascader
              v-model:value="categoryIds"
              :field-names="{label:'name',value:'id',children:'children'}"
              :options="levelTree"
              placeholder="请选择"
          />
        </a-form-item>
        <a-form-item label="描述">
          <a-input v-model:value="ebook.description" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>
<script>
import { defineComponent, onMounted, ref, reactive } from 'vue';
import axios from "axios";
import { message } from 'ant-design-vue';
import { array2Tree } from '@/util/tool'

const columns = [
  {
    title: '封面',
    dataIndex: 'cover',
    key:'cover',
  },
  {
    title: '名称',
    dataIndex: 'name',
    key: 'age',
  },
  /*{
    title: '分类一',
    dataIndex: 'category1Id',
  },
  {
    title: '分类二',
    dataIndex: 'category2Id',
  },*/
  {
    title: '分类',
    key: 'category',
  },
  {
    title: '文档数',
    dataIndex: 'docCount',
  },
  {
    title: '阅读数',
    dataIndex: 'viewCount',
  },
  {
    title: '点赞数',
    dataIndex: 'voteCount',
  },
  {
    title: '操作',
    key: 'action',
  },
];

export default defineComponent({
  name:'AdminEbook',
  components: {
  },
  setup() {

    const ebooks = ref()
    const queryForm = ref({
      name:null
    });
    
    const tableLoading = ref(false);
    const pagination = ref({
      current: 1,
      pageSize: 4,
      total: 0
    })

    /*弹窗*/
    const modalText = ref('Content of the modal');
    const modalVisible = ref(false);
    const modalLoading = ref(false);

    const handleOk = () => {
      modalLoading.value = true;
      ebook.value.category1Id = categoryIds.value[0];
      ebook.value.category2Id = categoryIds.value[1];

      axios.post("/ebook/save",ebook.value).then(res=>{
        modalLoading.value = false;

        if(res.data.success){
          modalVisible.value = false;

          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
        }else{

          message.warning(res.data.message);
        }

      })
    }

    /*表单*/
    const ebook = ref({
      data:{
        cover:'',
        name: '',
        category1Id: '',
        category2Id: '',
        description: '',
      }
    });

    const edit = record => {
      console.log(record)

      ebook.value = JSON.parse(JSON.stringify(record));
      categoryIds.value = [ebook.value.category1Id,ebook.value.category2Id];
      modalVisible.value = true;
    }

    const add = ()=>{
      ebook.value = {};
      modalVisible.value = true;
    }

    const del = id =>{
      axios.delete("/ebook/delete/" + id).then(res=>{
        if(res.data.success){
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
        }
      })
    }

    /*分页*/
    const pageChange = pageParam =>{
      console.log("分页参数",pageParam)
      handleQuery({
        page:pageParam.current,
        size:pageParam.pageSize
      })

      pagination.value.current = pageParam.current;
    }

    /*请求*/
    const handleQuery = params =>{
      tableLoading.value = true;
      axios.get("/ebook/list",{
        params:{
          page:params.page,
          size:params.size,
          name:queryForm.value.name
        },
      }).then(res=>{
        tableLoading.value = false;
        const data = res.data;
        if(data.success){
          ebooks.value = data.content.list;
          pagination.value.total = data.content.total;
        }else{
          message.warning(data.message)
        }
      })
    }

    const categoryIds = ref([]);
    const levelTree = ref([]);
    let categoryData ;

    /*分类数据*/
    const handleQueryCategory = () =>{
      tableLoading.value = true;
      axios.get("/category/all").then(res=>{
        tableLoading.value = false;
        const data = res.data;
        if(data.success){
          categoryData = data.content;
          levelTree.value = array2Tree(data.content,0);

          console.log("树形结构:",levelTree.value)
        }else{
          message.warning(data.message)
        }
      })
    }

    const getCategoryName = (cid)=>{
      let result = "";
      categoryData.forEach(ele=>{
        if(ele.id == cid){
          result = ele.name;
        }
      })
      return result;
    }

    onMounted( async ()=>{
      await handleQueryCategory();
      await handleQuery({
        page: 1,
        size: pagination.value.pageSize
      });
    })
    return {
      edit,
      add,
      del,
      ebook,
      pageChange,

      handleQuery,

      handleOk,
      modalText,
      modalVisible,
      modalLoading,
      pagination,
      tableLoading,
      queryForm,
      ebooks,
      columns,

      levelTree,
      categoryIds,
      getCategoryName,
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

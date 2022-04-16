<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <p>
        <a-button type="primary" @click="add()">新增</a-button>
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
        <a-form-item label="分类一">
          <a-input v-model:value="ebook.category1Id" />
        </a-form-item>
        <a-form-item label="分类二">
          <a-input v-model:value="ebook.category2Id" />
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
  {
    title: '分类一',
    dataIndex: 'category1Id',
  },
  {
    title: '分类二',
    dataIndex: 'category2Id',
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
      axios.post("/ebook/save",ebook.value).then(res=>{
        if(res.data.success){
          modalVisible.value = false;
          modalLoading.value = false;

          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
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
        params,
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

    onMounted(()=>{
      handleQuery({
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

      handleOk,
      modalText,
      modalVisible,
      modalLoading,
      pagination,
      tableLoading,
      ebooks,
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

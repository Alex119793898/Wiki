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
          :data-source="categorys"
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
        title="分类表单"
        v-model:visible="modalVisible"
        :confirm-loading="modalLoading"
        @ok="handleOk"
    >
      <a-form :model="category" :label-col="{span:6}">
        <a-form-item label="名称">
          <a-input v-model:value="category.name" />
        </a-form-item>
        <a-form-item label="父分类">
          <a-input v-model:value="category.parent" />
        </a-form-item>
        <a-form-item label="顺序">
          <a-input v-model:value="category.sort" />
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
    title: '名称',
    dataIndex: 'name',
    key: 'age',
  },
  {
    title: '父分类',
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

    const categorys = ref()
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
      axios.post("/category/save",category.value).then(res=>{
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
    const category = ref({
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

      category.value = JSON.parse(JSON.stringify(record));
      modalVisible.value = true;
    }

    const add = ()=>{
      category.value = {};
      modalVisible.value = true;
    }

    const del = id =>{
      axios.delete("/category/delete/" + id).then(res=>{
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
      axios.get("/category/list",{
        params:{
          page:params.page,
          size:params.size,
          name:queryForm.value.name
        },
      }).then(res=>{
        tableLoading.value = false;
        const data = res.data;
        if(data.success){
          categorys.value = data.content.list;
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
      category,
      pageChange,

      handleQuery,

      handleOk,
      modalText,
      modalVisible,
      modalLoading,
      pagination,
      tableLoading,
      queryForm,
      categorys,
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

<template>


  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-table
          :columns="columns"
          :data-source="ebooks"
          :pagination="pagination"
          :loading="loading"
          @change="pageChange"
      >
        <template #bodyCell="{column,record}">
          <template v-if="column.key == 'cover'">
            <img :src="record.cover" alt="avater">
          </template>
          <template v-else-if="column.key == 'action'">
            <a-space size="small">
              <a-button type="primary" @click="edit(record)">编辑</a-button>
              <a-button type="danger">删除</a-button>
            </a-space>

          </template>
        </template>
        <!--<template v-slot:cover="{record}">
          <img :src="record.cover" alt="avater">
        </template>-->
      </a-table>
    </a-layout-content>
  </a-layout>
</template>
<script>
import { defineComponent, onMounted, ref } from 'vue';
import axios from "axios";
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
    const pagination = ref({
      current: 1,
      pageSize: 2,
      total: 0
    })
    const loading = ref(false);

    const pageChange = pagination =>{
      console.log("分页参数",pagination)
      handleQuery({
        page:pagination.current,
        size:pagination.pageSize
      })
    }

    const handleQuery = params =>{
      loading.value = true;
      axios.get("/ebook/list",{
        params,
      }).then(res=>{
        loading.value = false;
        ebooks.value = res.data.content;
        pagination.value.total = res.data.content.length;

        pagination.value.current = params.page;
      })
    }

    const edit = record => {
      console.log(record)
    }

    onMounted(()=>{
      handleQuery({});
    })
    return {
      edit,
      pageChange,

      pagination,
      loading,
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

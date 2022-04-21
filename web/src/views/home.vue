<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
          mode="inline"
          :style="{ height: '100%', borderRight: 0 }"
          @click="handleMenuClick"
      >
        <a-menu-item key="welcome">
          <router-link to="/">
            <span>欢迎</span>
          </router-link>
        </a-menu-item>
        <a-sub-menu v-for="item in levelTree" :key="item.id">
          <template #title>
              <span>{{item.name}}</span>
          </template>
          <a-menu-item v-for="child in item.children" :key="child.id">{{child.name}}</a-menu-item>
        </a-sub-menu>
      </a-menu>
    </a-layout-sider>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <div class="welcome" v-show="isShowWelcome">
        <h1>欢迎页面</h1>
      </div>
      <a-list v-show="!isShowWelcome" :grid="{ gutter: 20, column: 3 }" :data-source="ebookList">
        <template #renderItem="{ item }">
          <a-list-item key="item.name">
            <template #actions>
              <span v-for="{ type, text } in actions" :key="type">
                <component :is="type" style="margin-right: 8px" />
                {{ text }}
              </span>
            </template>
            <a-list-item-meta :description="item.description">
              <template #title>
                <a :href="item.href">{{ item.name }}</a>
              </template>
              <template #avatar><a-avatar :src="item.cover" /></template>
            </a-list-item-meta>
          </a-list-item>
        </template>
      </a-list>

    </a-layout-content>
  </a-layout>
</template>

<script>
import { defineComponent, onMounted, ref, reactive, toRef } from 'vue';
import axios from 'axios';
import { message } from 'ant-design-vue';
import { array2Tree} from "@/util/tool";

export default defineComponent({
  name: 'HomeView',
  components: {

  },
  setup() {

    const isShowWelcome = ref(true);

    const ebookList = ref();
    const handleQuery = ()=>{
      axios.get("/ebook/list",{params:{page:1,size:1000}}).then(res=>{
        ebookList.value = res.data.content.list;
      })
    }

    const levelTree = ref([]);

    /*侧边栏菜单分类数据*/
    const handleQueryCategory = () =>{
      axios.get("/category/all").then(res=>{
        const data = res.data;
        if(data.success){
          levelTree.value = array2Tree(data.content,0);

          console.log("树形结构:",levelTree.value)
        }else{
          message.warning(data.message)
        }
      })
    }

    const handleMenuClick = (event)=>{
      console.log(event)
      if(event.key === 'welcome'){
        isShowWelcome.value = true;
      }else{
        isShowWelcome.value = false;
      }
    }

    onMounted(()=>{
      handleQuery();
      handleQueryCategory();
    })

    return {
      ebookList,
      levelTree,
      handleMenuClick,

      isShowWelcome,
      actions: [
        { type: 'StarOutlined', text: '156' },
        { type: 'LikeOutlined', text: '156' },
        { type: 'MessageOutlined', text: '2' },
      ],
    };


  },
});
</script>
<style scoped>
.ant-avatar {
  width: 50px;
  height: 50px;
  line-height: 50px;
  border-radius: 8%;
  margin: 5px 0;
}
</style>

<template>
  <a-layout-footer style="text-align: center">
    Ant Design ©2018 Created by Ant UED
    <!--{{obj.a}} {{params}} <span>{{list}}</span>-->

    <!--{{title}} {{num}}
    <span>
      <a v-for="item in categorys" :key="item">
        {{item}}
      </a>
    </span>-->
  </a-layout-footer>
</template>

<script lang="ts">
import {defineComponent, ref, reactive} from 'vue';

export default defineComponent({
  name: 'TheFooter',
  setup(){
    /*
    * 普通数据更新以后不会重新渲染视图
    * */
    let title = '商品分类';
    let num = 30;
    let categorys = ['女装', '男装', '家电'];

    /*
    * ref 使用规律
    * */
    const obj = ref({a:'yes'})
    obj.value = {a:'hello'}
    //console.log(obj)

    const params = ref(123);
    params.value = 321;
    //console.log(params)

    /*
    * reactive 使用规律
    * */
    const list = reactive({
      data:{ flag: false, content: [1,2,3], message: ''}
    })

    list.data = { flag: true, content: [4,5,6], message: 'save success'}
    //console.log(list)

    return{
      title,
      num,
      categorys,

      obj,
      params,
      list
    }
  }
});
</script>

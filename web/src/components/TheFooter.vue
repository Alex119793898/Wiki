<template>
  <a-layout-footer style="text-align: center">
    Ant Design ©2018 Created by Ant UED
    <!--{{obj.a}} {{params}} <span>{{list}}</span>-->
  </a-layout-footer>
</template>

<script lang="ts">
import {defineComponent, ref, reactive} from 'vue';

export default defineComponent({
  name: 'TheFooter',
  setup(){
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
      obj,
      params,
      list
    }
  }
});
</script>

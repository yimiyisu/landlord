<template>
  <z-form-items :fields="fields" />
</template>

<script>
export default {
  props: {
    formData: Object,
  },
  data() {
    return {
      fields: [],
      extraData: []
    };
  },
};
</script>
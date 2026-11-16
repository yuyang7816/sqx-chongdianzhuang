<template>
  <div
    class="site-wrapper nf-shell"
    :class="{ 'site-sidebar--fold': sidebarFold, 'nf-shell--ready': !loading }"
    v-loading.fullscreen.lock="loading"
    element-loading-text="正在加载系统..."
    element-loading-background="rgba(244, 246, 250, 0.88)">
    <template v-if="!loading">
      <main-navbar />
      <main-sidebar />
      <div
        class="site-content__wrapper nf-shell__content"
        :style="{ 'min-height': documentClientHeight + 'px' }">
        <main-content v-if="!$store.state.common.contentIsNeedRefresh" />
      </div>
    </template>
  </div>
</template>

<script>
  import MainNavbar from './main-navbar'
  import MainSidebar from './main-sidebar'
  import MainContent from './main-content'
  export default {
    provide () {
      return {
        // 刷新
        refresh () {
          this.$store.commit('common/updateContentIsNeedRefresh', true)
          this.$nextTick(() => {
            this.$store.commit('common/updateContentIsNeedRefresh', false)
          })
        }
      }
    },
    data () {
      return {
        loading: true
      }
    },
    components: {
      MainNavbar,
      MainSidebar,
      MainContent
    },
    computed: {
      documentClientHeight: {
        get () { return this.$store.state.common.documentClientHeight },
        set (val) { this.$store.commit('common/updateDocumentClientHeight', val) }
      },
      sidebarFold: {
        get () { return this.$store.state.common.sidebarFold }
      },
      userId: {
        get () { return this.$store.state.user.id },
        set (val) { this.$store.commit('user/updateId', val) }
      },
      userName: {
        get () { return this.$store.state.user.name },
        set (val) { this.$store.commit('user/updateName', val) }
      }
    },
    created () {
      this.getUserInfo()
    },
    mounted () {
      this.resetDocumentClientHeight()
    },
    methods: {
      // 重置窗口可视高度
      resetDocumentClientHeight () {
        this.documentClientHeight = document.documentElement['clientHeight']
        window.onresize = () => {
          this.documentClientHeight = document.documentElement['clientHeight']
        }
      },
      // 获取当前管理员信息
      getUserInfo () {
        this.$http({
          url: this.$http.adornUrl('/sys/user/info'),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.loading = false
            this.userId = data.user.userId
            this.userName = data.user.username
          }
        })
      }
    }
  }
</script>

<style lang="scss">
/* Shell 根布局 · yanglao-admin-v1 · Brand #5cc477 */
.nf-shell {
  position: relative;
  min-height: 100vh;
  overflow-x: hidden;
  background: var(--nf-page-bg, #f4f6fa);
}

.nf-shell__content {
  box-sizing: border-box;
}

.nf-shell--ready .nf-shell__content {
  animation: nf-shell-content-in 0.28s ease-out;
}

@keyframes nf-shell-content-in {
  from {
    opacity: 0;
    transform: translateY(4px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.nf-shell > .el-loading-mask {
  background-color: rgba(244, 246, 250, 0.9) !important;
}

.nf-shell > .el-loading-mask .el-loading-spinner .path {
  stroke: var(--nf-brand, #5cc477);
}

.nf-shell > .el-loading-mask .el-loading-text {
  color: var(--nf-brand-deep, #3fae5c);
  font-size: 14px;
  letter-spacing: 0.02em;
}
</style>

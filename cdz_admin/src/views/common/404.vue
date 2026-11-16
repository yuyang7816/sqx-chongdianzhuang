<template>
  <div class="site-wrapper site-page--not-found nf-404-page">
    <div class="nf-404-page__shell">
      <div class="nf-404-page__card">
        <div class="nf-404-page__icon-wrap">
          <i class="el-icon-warning-outline nf-404-page__icon"></i>
        </div>
        <div class="nf-404-page__hero">
          <span class="nf-page-hero__badge">页面异常</span>
          <div class="nf-404-page__code">404</div>
          <p class="nf-404-page__desc">抱歉！您访问的页面<em>失联</em>啦 ...</p>
          <div class="nf-page-hero__bar"></div>
        </div>
        <div class="nf-404-page__actions">
          <el-button icon="el-icon-back" @click="$router.go(-1)">返回上一页</el-button>
          <el-button type="primary" icon="el-icon-s-home" class="nf-404-btn-home" @click="$router.push({ name: 'home' })">进入首页</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
  }
</script>

<style lang="scss">
  .site-wrapper.site-page--not-found.nf-404-page {
    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    overflow: hidden;
    background: #f4f6fa;

    .site-content__wrapper {
      padding: 0;
      margin: 0;
      background-color: transparent;
    }

    .nf-404-page__shell {
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 16px;
      background: linear-gradient(135deg, #f4f6fa 0%, #ebf8ef 100%);
    }

    .nf-404-page__card {
      width: 100%;
      max-width: 480px;
      padding: 32px 28px 28px;
      text-align: center;
      background: #ffffff;
      border: 1px solid #e8eaed;
      border-radius: 10px;
      box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
    }

    .nf-404-page__icon-wrap {
      display: inline-flex;
      align-items: center;
      justify-content: center;
      width: 64px;
      height: 64px;
      margin-bottom: 16px;
      border-radius: 50%;
      background: #ebf8ef;
    }

    .nf-404-page__icon {
      font-size: 32px;
      color: #5cc477;
    }

    .nf-404-page__hero {
      margin-bottom: 24px;
    }

    .nf-page-hero__badge {
      display: inline-flex;
      align-items: center;
      padding: 2px 10px;
      border-radius: 999px;
      background: #ebf8ef;
      color: #5cc477;
      font-size: 12px;
      font-weight: 500;
      margin-bottom: 8px;
    }

    .nf-404-page__code {
      margin: 8px 0 12px;
      font-size: 72px;
      font-weight: 700;
      line-height: 1;
      letter-spacing: 2px;
      color: #1a2433;
    }

    .nf-404-page__desc {
      margin: 0;
      font-size: 15px;
      line-height: 1.6;
      color: #909399;

      > em {
        font-style: normal;
        color: #5cc477;
        font-weight: 500;
      }
    }

    .nf-page-hero__bar {
      height: 3px;
      width: 48px;
      margin: 16px auto 0;
      border-radius: 999px;
      background: #5cc477;
    }

    .nf-404-page__actions {
      display: flex;
      flex-wrap: wrap;
      align-items: center;
      justify-content: center;
      gap: 12px;
    }

    .nf-404-btn-home.el-button--primary {
      background-color: #5cc477 !important;
      border-color: #5cc477 !important;
      color: #fff !important;
    }

    .nf-404-btn-home.el-button--primary:hover,
    .nf-404-btn-home.el-button--primary:focus {
      background-color: #3fae5c !important;
      border-color: #3fae5c !important;
      color: #fff !important;
    }
  }
</style>

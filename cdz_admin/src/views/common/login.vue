<template>
  <div class="site-wrapper site-page--login login-page">
    <div class="login-shell">
      <section class="login-shell__brand">
        <div class="login-shell__brand-deco login-shell__brand-deco--1"></div>
        <div class="login-shell__brand-deco login-shell__brand-deco--2"></div>
        <div class="login-shell__brand-inner">
          <img class="login-shell__brand-logo" src="~@/assets/img/avatar.png" alt="logo">
          <span class="login-shell__brand-badge">管理系统</span>
          <h1 class="login-shell__brand-title">{{ entryName }}</h1>
          <p class="login-shell__brand-desc">高效、安全的管理平台，助力业务运营与数据管理。</p>
          <ul class="login-shell__features">
            <li><i class="el-icon-circle-check"></i> 数据安全加密传输</li>
            <li><i class="el-icon-circle-check"></i> 权限精细分级管控</li>
            <li><i class="el-icon-circle-check"></i> 运营数据实时可视</li>
          </ul>
        </div>
      </section>
      <section class="login-shell__panel">
        <div class="login-card">
          <div class="login-card__header">
            <h2 class="login-card__title">账号登录</h2>
            <p class="login-card__subtitle">请输入您的账号信息</p>
            <div class="login-card__accent"></div>
          </div>
          <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" status-icon>
            <el-form-item prop="userName">
              <el-input v-model="dataForm.userName" placeholder="帐号" prefix-icon="el-icon-user"></el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="dataForm.password" type="password" placeholder="密码" prefix-icon="el-icon-lock"></el-input>
            </el-form-item>
            <el-form-item prop="captcha">
              <el-row :gutter="12">
                <el-col :span="14">
                  <el-input v-model="dataForm.captcha" placeholder="验证码" prefix-icon="el-icon-picture-outline"></el-input>
                </el-col>
                <el-col :span="10" class="login-captcha">
                  <img :src="captchaPath" @click="getCaptcha()" alt="验证码">
                </el-col>
              </el-row>
            </el-form-item>
            <el-form-item>
              <el-button class="login-btn-submit nf-login-submit" type="primary" @click="dataFormSubmit()">登录</el-button>
            </el-form-item>
          </el-form>
        </div>
      </section>
    </div>
  </div>
</template>

<script>
  import { getUUID } from '@/utils'
  import { entryName } from '@/utils/httpRequest'
  export default {
    data () {
      return {
        dataForm: {
          userName: '',
          password: '',
          uuid: '',
          captcha: ''
        },
        dataRule: {
          userName: [
            { required: true, message: '帐号不能为空', trigger: 'blur' }
          ],
          password: [
            { required: true, message: '密码不能为空', trigger: 'blur' }
          ],
          captcha: [
            { required: true, message: '验证码不能为空', trigger: 'blur' }
          ]
        },
        captchaPath: '',
		show: false,
    entryName:'',
      }
    },
    created () {
       this.entryName = entryName
      this.getCaptcha()
    },
    methods: {
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl('sys/login'),
              method: 'post',
              data: this.$http.adornData({
                'username': this.dataForm.userName,
                'password': this.dataForm.password,
                'uuid': this.dataForm.uuid,
                'captcha': this.dataForm.captcha
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$cookie.set('token', data.token)
				this.getRouter()
              } else {
                this.getCaptcha()
                this.$message.error(data.msg)
              }
            })
          }
        })
      },
      getCaptcha () {
        this.dataForm.uuid = getUUID()
        this.captchaPath = this.$http.adornUrl(`captcha.jpg?uuid=${this.dataForm.uuid}`)
      },
	  xianshi () {
	    this.$http({
	      url: this.$http.adornUrl('app/common/type/261'),
	      method: 'get',
	      data: this.$http.adornData({})
	    }).then(({data}) => {
			if (data.code == 0) {
				if (data.data.value == '是') {
					this.show = true
				}
			}
	    })
	  },
	  getRouter(userId) {
	  	this.$http({
	  		url: this.$http.adornUrl(`sys/menu/nav`),
	  		method: 'get',
	  		params: this.$http.adornData()
	  	}).then(({
	  		data
	  	}) => {
	  		if (data && data.code === 0) {
	  			sessionStorage.setItem('menuList', JSON.stringify(data.menuList || '[]'))
	  			sessionStorage.setItem('permissions', JSON.stringify(data.permissions || '[]'))
	  			this.$router.replace({
	  				name: 'home'
	  			})
	  		} else {
	  			sessionStorage.setItem('menuList', '[]')
	  			sessionStorage.setItem('permissions', '[]')
	  		}
	  	})
	  },
    }
  }
</script>

<style lang="scss">
  $nf-brand: #5cc477;
  $nf-brand-deep: #3fae5c;
  $nf-brand-lite: #ebf8ef;
  $nf-page-bg: #f4f6fa;
  $nf-card-bg: #ffffff;
  $nf-border: #e8eaed;
  $nf-text-primary: #1a2433;
  $nf-text-secondary: #909399;

  .site-wrapper.site-page--login.login-page {
    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    overflow: hidden;
    min-width: auto;
    background: linear-gradient(135deg, $nf-page-bg 0%, $nf-brand-lite 100%);
  }

  .login-page .login-shell {
    min-height: 100vh;
    display: flex;
    align-items: stretch;
  }

  .login-page .login-shell__brand {
    position: relative;
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: center;
    padding: 48px;
    overflow: hidden;
    background: linear-gradient(145deg, $nf-brand 0%, $nf-brand-deep 100%);
    color: #fff;
  }

  .login-page .login-shell__brand-deco {
    position: absolute;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.08);
    pointer-events: none;
  }

  .login-page .login-shell__brand-deco--1 {
    width: 320px;
    height: 320px;
    top: -80px;
    right: -60px;
  }

  .login-page .login-shell__brand-deco--2 {
    width: 180px;
    height: 180px;
    bottom: 48px;
    left: -40px;
  }

  .login-page .login-shell__brand-inner {
    position: relative;
    z-index: 1;
    max-width: 420px;
  }

  .login-page .login-shell__brand-logo {
    width: 56px;
    height: 56px;
    border-radius: 14px;
    object-fit: cover;
    margin-bottom: 20px;
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
    border: 2px solid rgba(255, 255, 255, 0.3);
  }

  .login-page .login-shell__brand-badge {
    display: inline-flex;
    align-items: center;
    padding: 4px 12px;
    margin-bottom: 16px;
    border-radius: 999px;
    background: rgba(255, 255, 255, 0.18);
    font-size: 12px;
    font-weight: 500;
    letter-spacing: 0.5px;
  }

  .login-page .login-shell__brand-title {
    margin: 0 0 12px;
    font-size: 32px;
    font-weight: 700;
    line-height: 1.25;
  }

  .login-page .login-shell__brand-desc {
    margin: 0 0 28px;
    max-width: 360px;
    font-size: 15px;
    line-height: 1.7;
    opacity: 0.92;
  }

  .login-page .login-shell__features {
    margin: 0;
    padding: 0;
    list-style: none;
  }

  .login-page .login-shell__features li {
    display: flex;
    align-items: center;
    gap: 10px;
    margin-bottom: 12px;
    font-size: 14px;
    opacity: 0.95;
  }

  .login-page .login-shell__features li i {
    font-size: 16px;
    color: rgba(255, 255, 255, 0.85);
  }

  .login-page .login-shell__panel {
    width: 480px;
    max-width: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 48px 40px;
    background: $nf-page-bg;
  }

  .login-page .login-card {
    width: 100%;
    max-width: 380px;
    padding: 32px;
    background: $nf-card-bg;
    border-radius: 10px;
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
    border: 1px solid $nf-border;
  }

  .login-page .login-card__header {
    margin-bottom: 24px;
  }

  .login-page .login-card__title {
    margin: 0 0 6px;
    font-size: 24px;
    font-weight: 700;
    color: $nf-text-primary;
  }

  .login-page .login-card__subtitle {
    margin: 0 0 12px;
    font-size: 14px;
    color: $nf-text-secondary;
  }

  .login-page .login-card__accent {
    width: 40px;
    height: 3px;
    border-radius: 999px;
    background: $nf-brand;
  }

  .login-page .login-card .el-form-item {
    margin-bottom: 20px;
  }

  .login-page .login-card .el-form-item:last-child {
    margin-bottom: 0;
  }

  .login-page .login-card .el-input__inner {
    height: 44px;
    line-height: 44px;
    border-radius: 6px;
    border-color: $nf-border;
    transition: border-color 0.2s, box-shadow 0.2s;
  }

  .login-page .login-card .el-input__inner:focus {
    border-color: $nf-brand;
    box-shadow: 0 0 0 2px rgba(92, 196, 119, 0.15);
  }

  .login-page .login-card .el-input__prefix {
    left: 12px;
    color: $nf-text-secondary;
  }

  .login-page .login-card .el-input--prefix .el-input__inner {
    padding-left: 38px;
  }

  .login-page .login-card .login-btn-submit {
    width: 100%;
    height: 44px;
    margin-top: 4px;
    border-radius: 6px;
    font-size: 15px;
    font-weight: 600;
    letter-spacing: 1px;
    background-color: $nf-brand !important;
    border-color: $nf-brand !important;
    color: #fff !important;
  }

  .login-page .login-card .login-btn-submit:hover,
  .login-page .login-card .login-btn-submit:focus {
    background-color: $nf-brand-deep !important;
    border-color: $nf-brand-deep !important;
    color: #fff !important;
  }

  .login-page .login-captcha > img {
    display: block;
    width: 100%;
    height: 44px;
    object-fit: contain;
    object-position: center;
    background-color: #f5f7fa;
    border-radius: 6px;
    border: 1px solid $nf-border;
    cursor: pointer;
    transition: border-color 0.2s;
  }

  .login-page .login-captcha > img:hover {
    border-color: $nf-brand;
  }

  @media (max-width: 960px) {
    .login-page .login-shell {
      flex-direction: column;
    }

    .login-page .login-shell__brand {
      padding: 32px 24px;
    }

    .login-page .login-shell__brand-title {
      font-size: 26px;
    }

    .login-page .login-shell__features {
      display: none;
    }

    .login-page .login-shell__panel {
      width: 100%;
      padding: 24px 20px 32px;
    }

    .login-page .login-card {
      padding: 24px;
    }
  }
</style>

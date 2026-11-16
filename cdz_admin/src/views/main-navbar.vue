<template>
	<nav class="site-navbar" :class="'site-navbar--' + navbarLayoutType">
		<div class="site-navbar__header">
			<h1 class="site-navbar__brand">
				<a class="site-navbar__brand-lg" href="javascript:;">
					<img class="site-navbar__brand-logo" src="~@/assets/img/avatar.png" alt="logo">
					<span>{{ entryName }}</span>
				</a>
				<a class="site-navbar__brand-mini" href="javascript:;">
					<img class="site-navbar__brand-logo" src="~@/assets/img/avatar.png" alt="logo">
				</a>
			</h1>
		</div>
		<div class="site-navbar__body clearfix">
			<el-menu class="site-navbar__menu" mode="horizontal">
				<el-menu-item class="site-navbar__switch" index="0" @click="sidebarFold = !sidebarFold">
					<icon-svg name="zhedie"></icon-svg>
				</el-menu-item>
			</el-menu>
			<div class="site-navbar__context">
				<span class="site-navbar__context-badge">当前页面</span>
				<span class="site-navbar__context-title">{{ currentPageTitle }}</span>
			</div>
			<el-menu class="site-navbar__menu site-navbar__menu--right" mode="horizontal">
				<el-menu-item class="site-navbar__avatar" index="3">
					<el-dropdown :show-timeout="0" placement="bottom">
						<span class="el-dropdown-link user-pill">
							<img class="user-pill__avatar" src="~@/assets/img/avatar.png" :alt="userName">
							<span class="user-pill__meta">
								<span class="user-pill__name">{{ username || userName || '管理员' }}</span>
								<span class="user-pill__hint">欢迎回来</span>
							</span>
							<i class="el-icon-arrow-down user-pill__arrow"></i>
						</span>
						<el-dropdown-menu slot="dropdown">
							<el-dropdown-item @click.native="logoutHandle()">退出</el-dropdown-item>
						</el-dropdown-menu>
					</el-dropdown>
				</el-menu-item>
			</el-menu>
		</div>

		<update-password v-if="updatePassowrdVisible" ref="updatePassowrd"></update-password>
	</nav>
</template>

<script>
	import UpdatePassword from './main-navbar-update-password'
	import {
		clearLoginInfo
	} from '@/utils'
	import {
		entryName
	} from '@/utils/httpRequest'
	export default {
		data() {
			return {
				updatePassowrdVisible: false,
				username: '',
				intervalId: 0,
				storeId: 0,
				isshow: false,
				chatNum: 0,
				errCount: 0,
				code: 0,
				entryName: '',
				chatNumZx: 0,
				chatNumTs: 0,
				chatNumRz: 0,
				chatNumTx: 0,
				chatNumZydd: 0,
				chatNumDt: 0,
				chatNumHd: 0,
			}
		},
		components: {
			UpdatePassword
		},
		computed: {
			navbarLayoutType: {
				get() {
					return this.$store.state.common.navbarLayoutType
				}
			},
			sidebarFold: {
				get() {
					return this.$store.state.common.sidebarFold
				},
				set(val) {
					this.$store.commit('common/updateSidebarFold', val)
				}
			},
			mainTabs: {
				get() {
					return this.$store.state.common.mainTabs
				},
				set(val) {
					this.$store.commit('common/updateMainTabs', val)
				}
			},
			userName: {
				get() {
					return this.$store.state.user.name
				}
			},
			currentPageTitle() {
				if (this.$route && this.$route.meta && this.$route.meta.title) {
					return this.$route.meta.title
				}
				return '管理系统'
			}
		},
		methods: {
			updatePasswordHandle() {
				this.updatePassowrdVisible = true
				this.$nextTick(() => {
					this.$refs.updatePassowrd.init()
				})
			},
			logoutHandle() {
				this.$confirm(`确定进行[退出]操作?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$cookie.set('token', '')
					this.$cookie.set('userId', '')
					clearInterval(this.chatNum2)
					clearInterval(this.chatNum)
					this.delAllCookie()
					this.$router.push({
						name: 'login'
					})

				}).catch(() => {})
			},
			delAllCookie() {
				var keys = document.cookie.match(/[^ =;]+(?=\=)/g);
				if (keys) {
					for (var i = keys.length; i--;) {
						document.cookie =
							keys[i] + "=0;path=/;expires=" + new Date(0).toUTCString();
						document.cookie =
							keys[i] +
							"=0;path=/;domain=" +
							document.domain +
							";expires=" +
							new Date(0).toUTCString();
						document.cookie =
							keys[i] +
							"=0;path=/;expires=" +
							new Date(0).toUTCString();
					}
				}
			},
			userSelect() {
				let userId = this.$cookie.get('userId')
				this.$http({
					url: this.$http.adornUrl(`sys/user/info`),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					let returnData = data.user;
					this.username = returnData.username
				})
			},
			chatSelect() {
				this.$http({
					url: this.$http.adornUrl('chats/count'),
					method: 'get',
					params: this.$http.adornParams({
						'storeId': this.storeId
					})
				}).then(({
					data
				}) => {
					this.code = data.code
					console.log('this.chatNum', data.data)
					if (data.data > 0) {
						if (this.errCount != data.data) {
							this.$notify.info({
								title: '消息',
								dangerouslyUseHTMLString: true,
								message: '您有' + data.data + '条未读消息。',
								offset: 80,
								duration: 2500,
								type: "warning"
							});
							this.chatNum = data.data
							this.errCount = data.data
						}
					} else {
						this.chatNum = 0
						this.errCount = 0
					}
					if (this.chatNum == 0) {
						this.isshow = false
					} else {
						this.isshow = true
					}

				})
			},
			notification() {
				let that = this;
				if (!("Notification" in window)) {
					console.log("浏览器不支持消息通知");
					return;
				}
				Notification.requestPermission(function(permission) {});
			},
			chatSelect2() {
				this.$http({
					url: this.$http.adornUrl('chats/count'),
					method: 'get',
					params: this.$http.adornParams({
						'storeId': this.storeId
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					if (data.data > 0) {
						this.$notify.info({
							title: '消息',
							dangerouslyUseHTMLString: true,
							message: '您有' + data.data + '条未读消息。',
							offset: 80,
							duration: 2500,
							type: "warning"
						});
					}
				})
			},
			aplayAudio() {
				console.log('语音提示')
				document.getElementById('audio').play()

			},
			dataSelectZx() {
				this.$http({
					url: this.$http.adornUrl('message/selectMessageByType'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 10,
						'state': 4,
						'platform': '',
						'status': 0,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$cookie.set('numberZx', data.data.totalCount)
					}
				})
			},
			dataSelectTs() {
				this.$http({
					url: this.$http.adornUrl('message/selectMessageByType'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 10,
						'state': 3,
						'platform': '',
						'status': 0,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$cookie.set('numberTs', data.data.totalCount)
					}
				})
			},
			dataSelectRz() {
				this.$http({
					url: this.$http.adornUrl('userCertification/queryCertification'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 10,
						'status': 0,
						'userName': '',
						'name': '',
						'phone': '',
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$cookie.set('numberRz', data.data.totalCount)
					}
				})
			},
			dataSelectTx() {
				this.$http({
					url: this.$http.adornUrl('cash/selectPayDetails'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 10,
						'state': 0,
						'classify': '',
						'zhifubaoName': '',
						'zhifubao': '',
						'userType': '',
						'startTime': '',
						'endTime': '',
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$cookie.set('numberTx', data.data.totalCount)
					}
				})
			},
			dataSelectZydd() {
				this.$http({
					url: this.$http.adornUrl('shopOrders/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 0,
						'size': 10,
						'status': 2,
						'orderNum': '',
						'startTime': '',
						'endTime': '',
					})
				}).then(({
					data
				}) => {
					if (data.status == 0) {
						this.$cookie.set('numberZyDfh', data.data.totalElements)
					}
				})
				this.$http({
					url: this.$http.adornUrl('shopOrders/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 0,
						'size': 10,
						'status': 6,
						'orderNum': '',
						'startTime': '',
						'endTime': '',
					})
				}).then(({
					data
				}) => {
					if (data.status == 0) {
						this.$cookie.set('numberZyDtk', data.data.totalElements)
					}
				})
			},
			dataSelectDt() {
				this.$http({
					url: this.$http.adornUrl('gambit/selectTalkPage'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 0,
						'size': 10,
						'status': 1,
						'isDelete': 1,
						'address': '',
						'city': '',
						'userName': '',
						'content': '',
						'sort': '',
						'isRecommend': '',
					})
				}).then(({
					data
				}) => {
					if (data.status == 0) {
						this.$cookie.set('numberDt', data.data.page.total)
					}
				})
			},
			dataSelectHd() {
				this.$http({
					url: this.$http.adornUrl('activity/selectActivityList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 10,
						'status': 7,
						'userName': '',
						'phone': '',
						'city': '',
						'activityName': '',
						'activityType': '',
						'gameId': '',
						'time': '',
						'shopName': '',
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$cookie.set('numberDt', data.data.totalCount)
					}
				})
			},
		},
		mounted() {
			this.entryName = entryName
			this.userSelect()
		},
		created() {
		},
		beforeDestroy() {
			clearInterval(this.chatNum)
		},
		watch: {
			'chatNum': function() {
				if (this.chatNum > 0) {
					this.aplayAudio()
				}
			},
		}
	}
</script>
<style scoped="scoped">
	.site-navbar__avatar .el-dropdown-link > img {
		height: 36px;
	}

	.left-number {
		position: absolute;
		top: 0;
		right: 20px;
		width: 18px;
		height: 18px;
		line-height: 18px;
		border-radius: 50%;
		background: #ff4949;
		color: #ffffff;
		text-align: center;
		font-size: 10px;
		font-family: PingFang SC, PingFang SC-Regular;
		font-weight: 400;
		margin-top: 5px;
	}
</style>

<template>
	<el-submenu v-if="menu.list && menu.list.length >= 1" :index="menu.menuId + ''"
		:popper-class="'site-sidebar--' + sidebarLayoutSkin + '-popper'">
		<template slot="title">
			<el-badge class="item" v-if="menu.menuId==323&&show" is-dot>
				<icon-svg :name="menu.icon || ''" class="site-sidebar__menu-icon"></icon-svg>
				<span>{{ menu.name }}</span>
			</el-badge>
			<el-badge class="item" v-else-if="menu.menuId==325&&chatNumTx!=0" is-dot>
				<icon-svg :name="menu.icon || ''" class="site-sidebar__menu-icon"></icon-svg>
				<span>{{ menu.name }}</span>
			</el-badge>
			<el-badge class="item" v-else-if="menu.menuId==331&&chatNumZydd!=0" is-dot>
				<icon-svg :name="menu.icon || ''" class="site-sidebar__menu-icon"></icon-svg>
				<span>{{ menu.name }}</span>
			</el-badge>
			<div class="el-badge item" v-else>
			<icon-svg :name="menu.icon || ''" class="site-sidebar__menu-icon"></icon-svg>
			<span>{{ menu.name }}</span>
			</div>
		</template>
		<sub-menu  v-for="item in menu.list" :key="item.menuId" :menu="item" :dynamicMenuRoutes="dynamicMenuRoutes">
		</sub-menu>
	</el-submenu>
	<el-menu-item v-else :index="menu.menuId + ''" @click="gotoRouteHandle(menu)">
		<el-badge class="item" v-if="menu.menuId==206&&chatNumZx!=0" :value="chatNumZx">
			<icon-svg :name="menu.icon || ''" class="site-sidebar__menu-icon"></icon-svg>
			<span>{{ menu.name }}</span>
		</el-badge>
		
		<el-badge class="item" v-else-if="menu.menuId==41&&chatNumTs!=0" :value="chatNumTs">
			<icon-svg :name="menu.icon || ''" class="site-sidebar__menu-icon"></icon-svg>
			<span>{{ menu.name }}</span>
		</el-badge>
		<el-badge class="item" v-else-if="menu.menuId==282&&chatNumRz!=0" :value="chatNumRz">
			<icon-svg :name="menu.icon || ''" class="site-sidebar__menu-icon"></icon-svg>
			<span>{{ menu.name }}</span>
		</el-badge>
		
		
		<el-badge class="item" v-else-if="menu.menuId==34&&chatNumTx!=0" :value="chatNumTx">
			<icon-svg :name="menu.icon || ''" class="site-sidebar__menu-icon"></icon-svg>
			<span>{{ menu.name }}</span>
		</el-badge>
		
		<el-badge class="item" v-else-if="menu.menuId==358&&chatNumZydd!=0" :value="chatNumZydd">
			<icon-svg :name="menu.icon || ''" class="site-sidebar__menu-icon"></icon-svg>
			<span>{{ menu.name }}</span>
		</el-badge>
		<el-badge class="item" v-else-if="menu.menuId==360&&chatNumDt!=0" :value="chatNumDt">
			<icon-svg :name="menu.icon || ''" class="site-sidebar__menu-icon"></icon-svg>
			<span>{{ menu.name }}</span>
		</el-badge>
		<el-badge class="item" v-else-if="menu.menuId==265&&chatNumHd!=0" :value="chatNumHd">
			<icon-svg :name="menu.icon || ''" class="site-sidebar__menu-icon"></icon-svg>
			<span>{{ menu.name }}</span>
		</el-badge>
		<el-badge class="item" v-else-if="menu.menuId==266&&chatNumHd!=0" :value="chatNumHd">
			<icon-svg :name="menu.icon || ''" class="site-sidebar__menu-icon"></icon-svg>
			<span>{{ menu.name }}</span>
		</el-badge>
		
		<div class="el-badge item" v-else>
			<icon-svg :name="menu.icon || ''" class="site-sidebar__menu-icon aaaaaaa"></icon-svg>
			<span>{{ menu.name }}</span>
		</div>
	</el-menu-item>
</template>

<script>
	import SubMenu from './main-sidebar-sub-menu'
	export default {
		name: 'sub-menu',
		data(){
			return {
				chatNumZx:0,
				chatNumTs:0,
				chatNumRz:0,
				chatNumTx:0,
				chatNumZydd:0,
				chatNumDt:0,
				chatNumHd:0,
				show:false,
			}
		},
		props: {
			menu: {
				type: Object,
				required: true
			},
			dynamicMenuRoutes: {
				type: Array,
				required: true
			}
		},
		components: {
			SubMenu
		},
		computed: {
			sidebarLayoutSkin: {
				get() {
					return this.$store.state.common.sidebarLayoutSkin
				}
			}
		},
		methods: {
			// 通过menuId与动态(菜单)路由进行匹配跳转至指定路由
			gotoRouteHandle(menu) {
				var route = this.dynamicMenuRoutes.filter(item => item.meta.menuId === menu.menuId)
				if (route.length >= 1) {
					this.$router.push({
						name: route[0].name
					})
				}
			}
		},
		mounted() {
			setInterval(() => {
				if(this.$cookie.get('numberZx')){
					this.chatNumZx = this.$cookie.get('numberZx')
					if(Number(this.chatNumZx)>0){
						this.show = true
					}
				}
				if(this.$cookie.get('numberTs')){
					this.chatNumTs = this.$cookie.get('numberTs')
					if(Number(this.chatNumTs)>0){
						this.show = true
					}
				}
				if(this.$cookie.get('numberRz')){
					this.chatNumRz = this.$cookie.get('numberRz')
					if(Number(this.chatNumRz)>0){
						this.show = true
					}
				}
				if(this.$cookie.get('numberTx')){
					this.chatNumTx = this.$cookie.get('numberTx')
				}
				if(this.$cookie.get('numberZyDfh')||this.$cookie.get('numberZyDtk')){
					this.chatNumZydd = Number(this.$cookie.get('numberZyDfh'))+Number(this.$cookie.get('numberZyDtk'))
				}
				if(this.$cookie.get('numberDt')){
					this.chatNumDt = this.$cookie.get('numberDt')
				}
				if(this.$cookie.get('numberHd')){
					this.chatNumHd = this.$cookie.get('numberHd')
				}
				
			}, 1000)
		},
	}
</script>
<style>
	.item {
		/* margin-top: 10px;
		margin-right: 40px; */

	}

	.el-badge__content.is-fixed.is-dot {
		right: -5px;
		top: 20px;
	}
	.el-badge__content.is-fixed{
		right: 0px;
		top: 20px;
	}
</style>
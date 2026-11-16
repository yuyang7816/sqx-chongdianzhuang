<template>
	<div class="nf-page rider-top-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">教练管理</span>
			<h2 class="nf-page-hero__title">教练收益排行榜</h2>
			<p class="nf-page-hero__desc">查看教练订单数量与总收益排名，支持按教练昵称及时间范围筛选查询，点击昵称可跳转用户详情。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-search nf-filter-card__head-icon"></i>
				<span>筛选条件</span>
			</div>
			<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
				<!-- <el-form-item label="项目类型">
					<el-select
						class="rider-top-page__filter-select"
						v-model="classifyId"
						placeholder="请选择项目类型"
						@change="select()">
						<el-option v-for="item in fwData" :key="item.id" :label="item.value" :value="item.id">
						</el-option>
					</el-select>
				</el-form-item> -->
				<el-form-item label="教练昵称">
					<el-input
						class="rider-top-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入教练昵称"
						v-model="artificerName">
					</el-input>
				</el-form-item>
				<el-form-item label="开始时间">
					<el-date-picker
						class="rider-top-page__filter-date"
						v-model="startTime"
						align="right"
						type="datetime"
						format="yyyy-MM-dd"
						value-format="yyyy-MM-dd"
						placeholder="选择开始时间">
					</el-date-picker>
				</el-form-item>
				<el-form-item label="截止时间">
					<el-date-picker
						class="rider-top-page__filter-date"
						v-model="endTime"
						align="right"
						type="datetime"
						format="yyyy-MM-dd"
						value-format="yyyy-MM-dd"
						placeholder="选择截止时间">
					</el-date-picker>
				</el-form-item>
				<el-form-item class="rider-top-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="select">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
				</el-form-item>
			</el-form>
		</div>

		<div class="nf-table-card">
			<el-table v-loading="tableDataLoading" :data="homeData.list">
				<el-table-column label="编号" width="80">
					<template slot-scope="scope">
						<div>{{scope.$index+1}}</div>
					</template>
				</el-table-column>
				<el-table-column prop="artificerName" label="教练昵称">
					<template slot-scope="scope">
						<span class="rider-top-page__link"
							@click="updatesvideo(scope.row.userId)">{{scope.row.artificerName?scope.row.artificerName:'未绑定'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="counts" label="订单数量">
				</el-table-column>
				<el-table-column prop="money" label="总收益">
				</el-table-column>
				<!-- <el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column> -->
			</el-table>
			<div class="rider-top-page__pagination">
				<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
					:page-sizes="[10, 20, 30, 40]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="homeData.totalCount">
				</el-pagination>
			</div>
		</div>

	</div>
</template>

<script>
	import {
		quillEditor
	} from 'vue-quill-editor'
	import 'quill/dist/quill.core.css'
	import 'quill/dist/quill.snow.css'
	import 'quill/dist/quill.bubble.css'
	import quillConfig from '../selfShop/quill-config.js'
	import axios from 'axios';
	import {
		jsonp
	} from 'vue-jsonp'
	import {
		provinceAndCityData,
		regionData,
		provinceAndCityDataPlus,
		regionDataPlus,
		CodeToText,
		TextToCode
	} from 'element-china-area-data'
	var cityOptions = []
	var geocoder, map, markersArray = [];
	export default {
		components: {
			quillEditor
		},
		data() {
			return {
				size: 10,
				page: 1,
				limit: 10,
				classify: 6,
				type: '',
				method: 'false',
				tableDataLoading: true,
				homeData: {},
				
				choicenData: [],

				userId: '',
				search: '',
				myPhone: '',
				classifyIds: 0,
				statusId: 1,

				quillOption: quillConfig,
				
				// x新的
				indentState: 0, //任务状态
				info: {
					stockDate: this.getNowTime(), //日期
				},
				info1: {
					stockDate1: this.getNowTime1(), //日期
				},
				startTime: '',
				endTime: '',
				
				fwData:[],
				classifyId:'',
				artificerName:'',
			}
		},
		methods: {
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() //得到月份
				var date = now.getDate() //得到日期
				var hh = now.getHours() < 10 ? '0' + now.getHours() : now.getHours()
				var mm = now.getMinutes() < 10 ? '0' + now.getMinutes() : now.getMinutes()
				var ss = now.getSeconds() < 10 ? '0' + now.getSeconds() : now.getSeconds()
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
			},
			//处理默认选中当前日期
			getNowTime1() {
				var now = new Date();
				var year = now.getFullYear(); //得到年份
				var month = now.getMonth(); //得到月份
				var date = now.getDate(); //得到日期
				var hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
				var mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
				var ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
				month = month + 1;
				month = month.toString().padStart(2, "0");
				date = date.toString().padStart(2, "0");
				var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`;
				return defaultDate;
				this.$set(this.info, "stockDate", defaultDate);
			},
			handleSizeChange1(val) {
				this.limit = val
				this.homeSelect()
			},
			handleCurrentChange1(val) {
				this.page = val
				this.homeSelect()
			},
			
			// 获取社区数据列表
			homeSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('artificer/selectArtificerMoneyList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'startTime': this.startTime,
						'endTime': this.endTime,
						'classifyId':this.classifyId,
						'title':this.artificerName,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data

					console.log(data.data.state)
					this.homeData = data.data
				})
			},
			// 详情跳转
			updatesvideo(userId) {
				if (userId) {
					this.$router.push({
						path: '/userDetail',
						query: {
							userId: userId
						}
					})
				} else {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '未绑定用户',
						type: 'warning'
					})
					return
				}

			},
			// 查询
			select() {
				this.page = 1
				this.limit = 10
				this.homeSelect()
			},
			// 重置
			cleans() {
				this.classifyId = ''
				this.artificerName = ''
				this.address = ''
				this.flag = ''
				this.info.stockDate = this.getNowTime()
				this.homeSelect()
			},
			// 订单分析选择日期
			animeOrder() {
				console.log('info', this.info)
				this.homeSelect()
				// this.colonel()
			},
			// 订单分析年月日
			orderfenxi(value) {
				this.page = 1
				let vanumber = value
				// if (vanumber === 1) {
				this.flag = value
				this.homeSelect()
				// this.colonel()
				// }
				// if (vanumber === 2) {
				// 	this.flag = 2
				// 	this.taskData()
				// 	this.colonel()
				// }
				// if (vanumber === 3) {
				// 	this.flag = 3
				// 	this.taskData()
				// 	this.colonel()
				// }
			},
			// 项目类型
			fwSelect() {
				this.$http({
					url: this.$http.adornUrl('sys/dict/selectDictList'),
					method: 'get',
					params: this.$http.adornParams({
						'type': '服务类型',
						'status':1
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.fwData = returnData
				})
			},
		},
		mounted() {
			this.homeSelect()
			// this.fwSelect()
		}
	}
</script>

<style scoped>
.rider-top-page__filter-input {
	width: 200px;
}

.rider-top-page__filter-select {
	width: 150px;
}

.rider-top-page__filter-date {
	width: 160px;
}

.rider-top-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.rider-top-page__link {
	color: #5cc477;
	cursor: pointer;
}

.rider-top-page__link:hover {
	color: #3fae5c;
}

.rider-top-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}
</style>

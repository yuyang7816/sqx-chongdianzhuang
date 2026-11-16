<template>
	<div class="nf-page coupon-fk-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">发卡管理</span>
			<h2 class="nf-page-hero__title">发卡管理</h2>
			<p class="nf-page-hero__desc">管理发卡分类与卡密列表，支持添加分类、生成卡密、按卡密/用户/状态筛选，可查询、重置与批量删除。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-search nf-filter-card__head-icon"></i>
				<span>{{ activeName === 'first' ? '快捷操作' : '筛选条件' }}</span>
			</div>
			<div v-if="activeName === 'first'" class="nf-filter-card__body coupon-fk-page__filter-actions-only">
				<el-button type="primary" icon="el-icon-refresh" @click="refresh">刷新</el-button>
				<el-button type="primary" icon="el-icon-plus" :disabled="!isAuth('couponFk:add')" @click="amend()">添加</el-button>
			</div>
			<el-form v-else :inline="true" class="nf-filter-card__body" @submit.native.prevent>
				<el-form-item label="卡密">
					<el-input
						class="coupon-fk-page__filter-input"
						@keydown.enter.native="refreshList"
						clearable
						placeholder="请输入卡密"
						v-model="sdkContent">
					</el-input>
				</el-form-item>
				<el-form-item label="卡密名称">
					<el-input
						class="coupon-fk-page__filter-input"
						@keydown.enter.native="refreshList"
						clearable
						placeholder="请输入卡密名称"
						v-model="couponIssueNameT">
					</el-input>
				</el-form-item>
				<el-form-item label="领取用户">
					<el-input
						class="coupon-fk-page__filter-input"
						@keydown.enter.native="refreshList"
						clearable
						placeholder="请输入领取用户"
						v-model="userName">
					</el-input>
				</el-form-item>
				<el-form-item label="状态">
					<el-select
						class="coupon-fk-page__filter-select"
						v-model="status"
						placeholder="请选择状态"
						@change="refreshList(status)">
						<el-option v-for="item in statesnum" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="开始时间">
					<el-date-picker
						class="coupon-fk-page__filter-date"
						v-model="startTime"
						align="right"
						type="date"
						format="yyyy-MM-dd"
						value-format="yyyy-MM-dd"
						placeholder="选择开始时间">
					</el-date-picker>
				</el-form-item>
				<el-form-item label="截止时间">
					<el-date-picker
						class="coupon-fk-page__filter-date"
						v-model="endTime"
						align="right"
						type="date"
						format="yyyy-MM-dd"
						value-format="yyyy-MM-dd"
						placeholder="选择截止时间">
					</el-date-picker>
				</el-form-item>
				<el-form-item class="coupon-fk-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="refreshList">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
					<el-button
						type="primary"
						icon="el-icon-delete"
						@click="transferClcik()"
						:disabled="checkBoxData.length <= 0 || !isAuth('financeList:transfer')">
						批量删除
					</el-button>
				</el-form-item>
			</el-form>
		</div>

		<el-tabs v-model="activeName" @tab-click="handleClick" class="coupon-fk-page__tabs">
			<el-tab-pane label="发卡分类" name="first">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData.records">
					<el-table-column prop="typeId" label="编号" width="80">
					</el-table-column>
					<el-table-column label="卡密类型">
						<template slot-scope="scope">
							<span v-if="scope.row.sdkClassify == 1">金额</span>
							<span v-if="scope.row.sdkClassify == 2">优惠券</span>
						</template>
					</el-table-column>
					<el-table-column prop="couponIds" label="优惠券" width="200">
						<template slot-scope="scope">
							<div v-if="scope.row.selfCouponIssueList&&scope.row.selfCouponIssueList.length>0">
								<div v-for="(item,index) in scope.row.selfCouponIssueList" :key="index">{{item.couponName}}</div>
							</div>
							<div v-else>-</div>
						</template>
					</el-table-column>
					<el-table-column prop="remarks" label="名称">
					</el-table-column>
					<el-table-column prop="money" label="金额">
					</el-table-column>
					<el-table-column prop="validDay" label="有效天数">
					</el-table-column>
					<!-- <el-table-column label="状态">
						<template slot-scope="scope">
							<span style="color: #5cc477;" v-if="scope.row.status == 1">开启</span>
							<span v-if="scope.row.status == 2">关闭</span>
						</template>
					</el-table-column> -->
					<el-table-column prop="createTime" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" width="200">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('couponFk:update')"
								@click="amend(scope.row)" style="margin: 3px;">编辑
							</el-button>
							<el-button size="mini" type="warning" @click="listBtnKm(scope.row)"
								style="margin: 3px;">卡密列表
							</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('couponFk:delete')"
								@click="deleteStair(scope.row)" style="margin: 3px;">删除
							</el-button>
							<el-button :disabled="!isAuth('couponFk:update')" size="mini" type="primary" icon="document"
								@click="shengCoupon(scope.row)" style="margin: 3px;">生成卡密</el-button>

						</template>
					</el-table-column>
				</el-table>
				<div class="coupon-fk-page__pagination">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tableData.total">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>

			<el-tab-pane label="发卡列表" name="putong">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoadingKm" :data="tableDataKm.records" @selection-change="changeFunP">
					<el-table-column type="selection">
					</el-table-column>
					<el-table-column prop="sdkId" fixed="left" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="sdkRemarks" label="卡密名称">
					</el-table-column>
					<el-table-column prop="sdkContent" label="卡密">
					</el-table-column>
					<!-- <el-table-column prop="sysUserName" label="渠道商名称">
          </el-table-column> -->
					<el-table-column prop="money" label="金额">
					</el-table-column>
					<el-table-column prop="overdueTime" label="到期时间">
					</el-table-column>
					<el-table-column label="领取用户">
						<template slot-scope="scope">
							<span class="coupon-fk-page__link" @click="updates(scope.row)"
								v-if="scope.row.nickName">{{scope.row.nickName}}</span>
							<span v-else>未绑定</span>
						</template>
					</el-table-column>
					<el-table-column label="状态">
						<template slot-scope="scope">
							<span class="coupon-fk-page__status-pending" v-if="scope.row.status == 0">待使用</span>
							<span class="coupon-fk-page__status-used" v-if="scope.row.status == 1">已使用</span>
							<span class="coupon-fk-page__status-expired" v-if="scope.row.status == 2">已过期</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="150">
					</el-table-column>
					<el-table-column label="操作" width="80">
						<template slot-scope="scope">
							<el-button size="mini" type="danger" :disabled="!isAuth('couponFk:delete')"
								@click="deleteStairKm(scope.row)" style="margin: 3px;">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="coupon-fk-page__pagination">
					<el-pagination @size-change="handleSizeChangeList" @current-change="handleCurrentChangeList"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tableDataKm.total">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<!-- 修改 -->
			<el-dialog :title="titles" :visible.sync="dialogFormVisible" center>
				<el-form :model="form">
					<el-form-item label="类型：" :label-width="formLabelWidth">
						<el-radio-group v-model="form.sdkClassify">
							<el-radio :label="1">金额</el-radio>
							<el-radio :label="2">优惠券</el-radio>
						</el-radio-group>
					</el-form-item>
					<el-form-item label="优惠券：" :label-width="formLabelWidth" v-if="form.sdkClassify==2">
						<el-tag :key="j" v-for="(tag,j) in couponIdList" closable :disable-transitions="false"
							@close="couponIdClose1(tag,j)">
							{{tag.couponName}}
						</el-tag>
						<el-button class="button-new-tag" size="small" @click="couponIdBtn1()">添加</el-button>
					</el-form-item>
					<el-form-item label="名称：" :label-width="formLabelWidth">
						<el-input v-model="form.remarks" style="width:65%;" placeholder="请输入名称"></el-input>
					</el-form-item>
					<el-form-item label="金额：" :label-width="formLabelWidth" v-if="form.sdkClassify==1">
						<el-input v-model="form.money" style="width:65%;" placeholder="请输入金额"></el-input>
					</el-form-item>
					<el-form-item label="有效天数：" :label-width="formLabelWidth" v-if="form.sdkClassify==1">
						<el-input v-model="form.validDay" style="width:65%;" placeholder="请输入有效天数"></el-input>
					</el-form-item>
				</el-form>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible = false">取 消</el-button>
					<el-button type="primary" @click="amendNoticeTo()">确 定</el-button>
				</div>
			</el-dialog>

			<!-- 生成卡密 -->
			<el-dialog title="生成卡密" :visible.sync="dialogFormVisible3" center width="70%">
				<!-- <div style="margin-bottom: 10px;">
          <span style="width: 200px;display: inline-block;text-align: right;">渠道商：</span>
          <el-input style="width:50%;" v-model="sysUserName" @focus="qudaoBtn" placeholder="请选择渠道商">
          </el-input>
        </div> -->
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">生成数量：</span>
					<el-input style="width:50%;" v-model="num" type="number" :min="0" placeholder="请输入生成数量">
					</el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible3 = false">取 消</el-button>
					<el-button type="primary" @click="couponNoticeTo()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 卡密列表 -->
			<el-dialog title="卡密列表" :visible.sync="dialogFormVisibleKm" center width="70%">
				<div style="display: inline-block;margin: 3px;">
					<div style="position: relative;display: inline-block;">
						<span>领取用户：</span>
						<el-input style="width: 200px;" @keydown.enter.native="refreshList" placeholder="请输入领取用户"
							v-model="userName">
						</el-input>&nbsp;&nbsp;
					</div>

					<div style="margin:2% 0;display: inline-block;">
						<span>状态：</span>
						<el-select v-model="status" style="width:150px;margin-left: 10px;"
							@change="refreshList(status)">
							<el-option v-for="item in statesnum" :key="item.value" :label="item.label"
								:value="item.value">
							</el-option>
						</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
					<span>开始时间：</span>
					<el-date-picker style="width: 160px;margin-left: 10px;" v-model="startTime" align="right"
						type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间">
					</el-date-picker>&nbsp;&nbsp;&nbsp;
					<span>截止时间：</span>
					<el-date-picker style="width: 160px;margin-left: 10px;" v-model="endTime" align="right" type="date"
						format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择截止时间">
					</el-date-picker>
				</div>
				<div style="display: inline-block;">
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document"
						@click="refreshList">查询
					</el-button>
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="cleans">重置
					</el-button>
					<el-button size="mini" type="warning" :disabled="!isAuth('couponFk:delete')" @click="excelStair()"
						style="margin: 3px;">导出excel
					</el-button>
				</div>
				<el-table v-loading="tableDataLoadingKm" :data="tableDataKm.records">
					<el-table-column prop="sdkId" fixed="left" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="sdkRemarks" label="卡密名称">
					</el-table-column>
					<el-table-column prop="sdkContent" label="卡密">
					</el-table-column>
					<el-table-column prop="money" label="金额" width="80">
					</el-table-column>
					<el-table-column prop="overdueTime" label="到期时间">
					</el-table-column>
					<el-table-column label="领取用户">
						<template slot-scope="scope">
							<span class="coupon-fk-page__link" @click="updates(scope.row)"
								v-if="scope.row.nickName">{{scope.row.nickName}}</span>
							<span v-else>未绑定</span>
						</template>
					</el-table-column>
					<el-table-column label="状态">
						<template slot-scope="scope">
							<span class="coupon-fk-page__status-pending" v-if="scope.row.status == 0">待使用</span>
							<span class="coupon-fk-page__status-used" v-if="scope.row.status == 1">已使用</span>
							<span class="coupon-fk-page__status-expired" v-if="scope.row.status == 2">已过期</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="领取时间" width="150">
					</el-table-column>
					<el-table-column label="操作" width="80">
						<template slot-scope="scope">
							<el-button size="mini" type="danger" :disabled="!isAuth('couponFk:delete')"
								@click="deleteStairKm(scope.row)" style="margin: 3px;">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeList" @current-change="handleCurrentChangeList"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tableDataKm.total">
					</el-pagination>
				</div>
			</el-dialog>
			<!-- 渠道商 -->
			<el-dialog title="渠道商" :visible.sync="dialogFormVisibleQd" center width="70%">
				<el-table :data="dataList" v-loading="dataListLoading" style="width: 100%;">
					<el-table-column prop="userId" header-align="center" align="center" width="80" label="ID">
					</el-table-column>
					<el-table-column prop="username" header-align="center" align="center" label="用户名">
					</el-table-column>
					<el-table-column prop="email" header-align="center" align="center" label="邮箱">
					</el-table-column>
					<el-table-column prop="mobile" header-align="center" align="center" label="手机号">
					</el-table-column>
					<el-table-column prop="qdRate" header-align="center" align="center" label="渠道佣金">
						<!-- <template slot-scope="scope">
							<span  v-for="(item,index) in scope.row.roleEntityList" :key="index">{{item.roleName}} </span>
						</template> -->
					</el-table-column>
					<el-table-column prop="qdCode" header-align="center" align="center" label="渠道码">
						<template slot-scope="scope">
							<span v-if="scope.row.qdCode">{{scope.row.qdCode}}</span>
							<span v-else> - </span>
						</template>
					</el-table-column>
					<el-table-column prop="status" header-align="center" align="center" label="状态">
						<template slot-scope="scope">
							<el-tag v-if="scope.row.status === 0" size="small" type="danger">禁用</el-tag>
							<el-tag v-else size="small">正常</el-tag>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" header-align="center" align="center" width="180" label="创建时间">
					</el-table-column>
					<el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
						<template slot-scope="scope">
							<el-button type="text" size="small" @click="addOrUpdateHandle(scope.row)">确认</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-dialog>
			<!-- 优惠券列表 -->
			<el-dialog title="优惠券列表" :visible.sync="dialogFormVisible5" center width="70%">
				<div style="display: inline-block;margin-top: 5px;">
					<span>类型：</span>
					<el-select v-model="typeY" style="width:150px;margin-left: 10px;" @change="select1()">
						<el-option v-for="item in typeList" :key="item.value" :label="item.title" :value="item.value">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;
				</div>
				<!-- <div style="display: inline-block;margin-top: 5px;">
					<span>状态：</span>
					<el-select v-model="statusY" style="width:150px;margin-left: 10px;" @change="select1()">
						<el-option v-for="item in memberList" :key="item.value" :label="item.title" :value="item.value">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;
				</div> -->
				<div style="display: inline-block;margin-top: 5px;">
					<span>优惠券名称：</span>
					<el-input style="width: 150px;" @keydown.enter.native="select1" clearable placeholder="请输入优惠券名称"
						v-model="couponNameY">
					</el-input>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="display: inline-block;margin-top: 5px;">
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="select1">查询
					</el-button>
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleans1">重置
					</el-button>
				</div>
				<el-button style="margin-left:15px;" :disabled="xzList.length <= 0 " size="mini" type="primary"
					icon="document" @click="closes1()">确定</el-button>
				<el-table v-loading="tableDataLoadingY" @selection-change="changeFun1" :data="tableDataYhq.content">
					<el-table-column type="selection" fixed>
					</el-table-column>
					<el-table-column prop="couponIssueId" label="编号" width="80" fixed="left">
					</el-table-column>
					<el-table-column prop="type" label="类型" fixed="left">
						<template slot-scope="scope">
							<span v-if="scope.row.type == 1">团购券</span>
							<span v-if="scope.row.type == 2">积分兑换券</span>
							<span v-if="scope.row.type == 3">新人券</span>
						</template>
					</el-table-column>
					<el-table-column prop="couponName" label="优惠券名称" width="150" fixed="left">
					</el-table-column>
					<el-table-column prop="issueNumber" label="发布数量">
					</el-table-column>
					<el-table-column prop="remainNumber" label="剩余数量">
					</el-table-column>
					<!-- <el-table-column prop="integral" label="兑换积分">
					</el-table-column> -->
					<el-table-column prop="hour" label="使用时长">
					</el-table-column>
					<el-table-column prop="validDay" label="有效期限(天)">
					</el-table-column>
					<el-table-column prop="teamRule" label="使用规则" width="250">
						<template slot-scope="scope">
							<el-popover placement="top-start" title="" trigger="hover">
								<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"  slot="reference">
									{{scope.row.teamRule}}
								</div>
								<div style="width: 500px;height: auto;">{{scope.row.teamRule}}</div>
							</el-popover>
						</template>
					</el-table-column>
					<!-- <el-table-column prop="status" label="是否是会员">
						<template slot-scope="scope">
							<span v-if="scope.row.isVip==1">是</span>
							<span v-else>否</span>
						</template>
					</el-table-column> -->
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size1" :current-page="page1"
						layout="total,sizes, prev, pager, next,jumper" :total="tableDataYhq.totalElements">
					</el-pagination>
				</div>
			</el-dialog>

		</el-tabs>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				limit: 10,
				page: 1,
				size1: 10,
				page1: 1,
				form: {
					typeId: '',
					money: '',
					remarks: '',
					validDay: '',
					sdkClassify: 1,
					couponIds: [],
				},
				formLabelWidth: '200px',
				tableDataLoading: false,
				tableDataLoadingKm: false,
				dialogFormVisible: false,
				tableData: {},
				tableDataKm: {},
				activeName: 'first',
				classify: 1,
				couponIssueNameT: '',
				statusT: 0,
				couponIds: [],
				couponIdList: [],
				dialogFormVisible4: false,
				dialogFormVisible5: false,
				tableDataLoadingY: false,
				tableDataYhq: {},
				typeY: 1,
				statusY: '',
				couponNameY: '',
				xzList: [],
				memberList: [{
						value: '',
						title: '全部'
					},
					{
						value: 1,
						title: '开启'
					},
					{
						value: 2,
						title: '关闭'
					},
				],
				typeList: [
					// {
					// 	value: '',
					// 	title: '全部'
					// },
					{
						value: 1,
						title: '团购券'
					},
					{
						value: 2,
						title: '积分兑换券'
					},
					{
						value: 3,
						title: '新人券'
					},
				],

				titles: '添加',
				tableDataList: [],
				dialogFormVisible3: false,
				num: 0,
				couponCardTypeId: '',
				status: '',
				statesnum: [{
						label: '全部',
						value: ''
					},
					{
						label: '待使用',
						value: 0
					},
					{
						label: '已使用',
						value: 1
					},
					{
						label: '已过期',
						value: 2
					}
				],
				dialogFormVisibleKm: false,
				checkBoxData: [], //多选框选择的值
				userName: '',
				sdkContent: '',
				startTime: '',
				endTime: '',
				info: {
					stockDate: this.getNowTime(), //日期
				},
				info2: {
					stockDate2: this.getNowTime2(), //日期
				},
				dialogFormVisibleQd: false,
				dataList: [],
				dataListLoading: false,
				sysUserId: '',
				sysUserName: '',
				sysUserNameT: '',
			}
		},
		methods: {
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() //得到月份
				var date = now.getDate() //得到日期
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
			},
			//处理默认选中当前日期
			getNowTime2() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() - now.getMonth() //得到月份
				var date = now.getDate() - now.getDate() + 1 //得到日期
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
			},
			// 多选
			changeFunP(val) {
				this.checkBoxData = val;
			},
			handleSizeChange(val) {
				this.limit = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			handleSizeChange(val) {
				this.size = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			handleSizeChangeList(val) {
				this.limit = val;
				this.dataSelectKm()
			},
			handleCurrentChangeList(val) {
				this.page = val;
				this.dataSelectKm()
			},

			handleSizeChangeY(val) {
				this.size1 = val;
				this.dataSelectYhq()
			},
			handleCurrentChangeY(val) {
				this.page1 = val;
				this.dataSelectYhq()
			},
			handleClick(tab, event) {
				this.limit = 10
				this.page = 1
				this.couponCardTypeId = ''
				if (tab._props.label == '发卡分类') {
					this.classify = 1
					this.dataSelect()
				}
				if (tab._props.label == '发卡列表') {
					this.dataSelectKm()
				}

			},
			// 刷新
			refresh() {
				this.page = 1
				this.dataSelect()
			},
			// 添加、修改
			amend(row) {
				this.dialogFormVisible = true
				
				if (row) {
					this.titles = '修改'
					this.form.typeId = row.typeId
					this.form.money = row.money
					this.form.remarks = row.remarks
					this.form.validDay = row.validDay
					if(row.couponIds){
						this.couponIds = row.couponIds.split(',')
					}else{
						this.couponIds = []
					}
					if(row.selfCouponIssueList){
						this.couponIdList = row.selfCouponIssueList
					}else{
						this.couponIdList = []
					}
					this.form.sdkClassify = row.sdkClassify
					
				} else {
					this.titles = '添加'
					this.form.typeId = ''
					this.form.money = ''
					this.form.remarks = ''
					this.form.validDay = ''
					this.couponIds = []
					this.couponIdList = []
					this.form.sdkClassify = 1
				}

			},
			handleAvatarSuccess2(file, fileList) {
				this.form.couponIssueImage = file.data
			},
			// 修改
			amendNoticeTo() {
				if (this.form.remarks === '') {
					this.$notify({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '请输入名称',
						type: 'warning'
					});
					return
				}
				
				
				if(this.form.sdkClassify==1){
					if (this.form.money === '' || this.form.money == null) {
						this.$notify({
							title: '提示',
							type: 'error',
							duration: 1800,
							message: '请输入金额',
							type: 'warning'
						});
						return
					}
					if (this.form.validDay === ''||this.form.validDay ==null) {
						this.$notify({
							title: '提示',
							type: 'error',
							duration: 1800,
							message: '请输入有效天数',
							type: 'warning'
						});
						return
					}
				}else{
					if(this.couponIdList.length==0){
						this.$notify({
							title: '提示',
							type: 'error',
							duration: 1800,
							message: '请选择优惠券',
							type: 'warning'
						});
						return
					}
				}
				

				if (this.titles == '添加') {
					var url = 'couponCard/insertCouponCardType'
				} else {
					var url = 'couponCard/updateCouponCardType'
				}

				this.$http({
					url: this.$http.adornUrl('admin/sdkType/saveSdkType'),
					method: 'post',
					// data: this.$http.adornData({
					params: this.$http.adornParams({
						'typeId': this.form.typeId,
						'money': this.form.money,
						'remarks': this.form.remarks,
						'validDay': this.form.validDay,
						'couponIds':this.couponIds.toString(),
						'sdkClassify':this.form.sdkClassify,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
						this.dialogFormVisible = false
					} else {
						this.$message({
							message: data.msg,
							type: 'success',
							duration: 1500,
							onClose: () => {}
						})
					}

				})
			},
			//删除
			deleteStair(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/sdkType/deleteSdkType'),
						method: 'get',
						params: this.$http.adornParams({
							'typeId': row.typeId
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						}

					})
				}).catch(() => {})
			},
			refreshList() {
				this.page = 1
				this.dataSelectKm()
			},
			// 重置
			cleans() {
				this.endTime = ''
				this.startTime = ''
				this.couponIssueNameT = ''
				this.sdkContent = ''
				this.status = ''
				this.userName = ''
				this.sysUserNameT = ''
				this.page = 1
				this.dataSelectKm()
			},
			// 获取卡密分类数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/sdkType/getSdkTypeList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'remarks': this.content
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
					if(this.page>1){
						if (data.data.records.length == 0) {
							this.page = this.page - 1
							this.dataSelect()
						}
					}
					
				})
			},

			// 批量开启
			closes2(id) {
				this.dialogFormVisible4 = false
			},
			// 多选券
			changeFun(row) {
				this.couponIds.push(row.couponIssueId)
				this.couponIdList.push(row.couponName)
				this.dialogFormVisible4 = false
			},
			// 优惠券列表
			listBtn(row) {
				this.tableDataList = row.couponList
				this.dialogFormVisible5 = true
			},
			// 生成卡密弹框
			shengCoupon(row) {
				this.num = ''
				this.sysUserId = ''
				this.sysUserName = ''
				this.couponCardTypeId = row.typeId
				this.dialogFormVisible3 = true
			},
			// 确认生成
			couponNoticeTo() {
				// if (this.sysUserId == '') {
				//   this.$notify({
				//     title: '提示',
				//     duration: 1800,
				//     message: '请选择渠道商',
				//     type: 'warning'
				//   });
				//   return
				// }
				if (this.num == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入生成数量',
						type: 'warning'
					});
					return
				}
				if (this.num > 100) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '单次生成数量数量最高为100',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('admin/sdkInfo/saveSdkInfo'),
					method: 'post',
					params: this.$http.adornParams({
						'typeId': this.couponCardTypeId,
						'num': this.num,
						// 'sysUserId': this.sysUserId,

					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dialogFormVisible3 = false
								this.dataSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					}

				})
			},
			// 卡密列表
			dataSelectKm() {
				this.tableDataLoadingKm = true
				// if (this.endTime == '') {
				// 	this.endTime = this.info.stockDate
				// }
				// if (this.startTime == '') {
				// 	this.startTime = this.info2.stockDate2
				// }
				this.$http({
					url: this.$http.adornUrl('admin/sdkInfo/getSdkList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'sdkRemarks': this.couponIssueNameT,
						'status': this.status,
						'typeId': this.couponCardTypeId,
						'nickName': this.userName,
						'sdkContent': this.sdkContent,
						'endTime': this.endTime,
						'startTime': this.startTime,
						// 'sysUserName': this.sysUserNameT,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingKm = false
					let returnData = data.data;
					this.tableDataKm = returnData

				})
			},
			// 详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
			// 卡密列表弹框
			listBtnKm(row) {
				this.couponCardTypeId = row.typeId
				this.couponIssueNameT = ''
				this.sdkContent = ''
				this.status = ''
				this.userName = ''
				this.sysUserNameT = ''
				this.dataSelectKm()
				this.dialogFormVisibleKm = true
			},

			// 导出
			excelStair() {
				// var endTime = this.endTime
				// if (this.endTime != '') {
				// 	endTime = this.endTime + " 23:59:59"
				// }
				this.$http({
					url: this.$http.adornUrl('admin/sdkInfo/excelCouponCardList'),
					method: 'get',
					responseType: 'blob',
					params: this.$http.adornParams({
						'sdkRemarks': this.couponIssueNameT,
						'status': this.status,
						'typeId': this.couponCardTypeId,
						'nickName': this.userName,
						'sdkContent': this.sdkContent,
						'endTime': this.endTime,
						'startTime': this.startTime,
						// 'sysUserName': this.sysUserNameT

					})
				}).then(({
					data
				}) => {
					let blob = new Blob([data], {
						type: 'application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
					})
					if (window.navigator.msSaveOrOpenBlob) {
						navigator.msSaveBlob(blob)
					} else {
						let url = window.URL.createObjectURL(blob)
						let elink = document.createElement('a')
						elink.download = '卡密列表.xlsx'
						elink.style.display = 'none'
						elink.href = url
						document.body.appendChild(elink)
						elink.click()
						document.body.removeChild(elink)
					}
				})
			},
			//批量删除
			deleteStairKm(row) {
				var sdkIds = []
				sdkIds.push(row.sdkId)
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/sdkInfo/deleteSdk'),
						method: 'get',
						params: this.$http.adornParams({
							'sdkIds': row.sdkId,
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelectKm()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 1500,
								onClose: () => {
									this.dataSelectKm()
								}
							})
						}

					})
				}).catch(() => {})
			},
			// 批量转账
			transferClcik(id) {
				var ids = id ? [id] : this.checkBoxData.map(item => {
					return item.sdkId
				})
				this.$http({
					url: this.$http.adornUrl('admin/sdkInfo/deleteSdk'),
					method: 'get',
					params: this.$http.adornParams({
						'sdkIds': ids.toString(),
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelectKm()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
							onClose: () => {
								this.dataSelectKm()
							}
						})
					}
				})
			},
			// 渠道商弹框
			qudaoBtn() {
				this.getDataList()
				this.dialogFormVisibleQd = true

			},
			getDataList() {
				this.dataListLoading = true
				this.$http({
					url: this.$http.adornUrl('sys/user/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 10,
						'username': '',
						'isChannel': 1
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.dataList = data.page.list
					} else {
						this.dataList = []
					}
					this.dataListLoading = false
				})
			},
			// 确认选择渠道商
			addOrUpdateHandle(row) {
				this.sysUserId = row.userId
				this.sysUserName = row.username
				this.dialogFormVisibleQd = false
			},
			// 打开用户列表
			couponIdBtn1(rows) {
				// this.couponIds = []
				// this.couponIdList = []
				this.page1 = 1
				this.typeY = 1
				this.statusY = ''
				this.couponNameY = ''
				this.dataSelectYhq()

				this.dialogFormVisible5 = true
			},
			// 获取优惠券数据列表
			dataSelectYhq() {
				this.tableDataLoadingY = true
				this.$http({   
					url: this.$http.adornUrl('selfCouponIssue/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1 - 1,
						'size': this.size1,
						'type': this.typeY,
						'tearoomName': '',
						'couponName': this.couponNameY,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingY = false
					let returnData = data.data
					this.tableDataYhq = returnData
				})
			},
			handleSizeChange2(val) {
				this.size1 = val;
				this.dataSelectYhq()
			},
			handleCurrentChange2(val) {
				this.page1 = val;
				this.dataSelectYhq()
			},
			// 查询
			select1() {
				this.page1 = 1
				this.dataSelectYhq()
			},
			// 重置
			cleans1() {
				this.typeY = 1
				this.statusY = ''
				this.couponNameY = ''
				this.dataSelectYhq()
			},
			// 删除用户
			couponIdClose1(ietm, index) {
				console.log('index', index, 'ietm', ietm, this.couponIds.length)
				if (this.couponIds.length < 2) {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '最少需要选中一张优惠券',
						type: 'warning'
					});
					return
				} else {
					this.couponIds.splice(index, 1)
					this.couponIdList.splice(index, 1)
				}
			},
			// 多选用户
			changeFun1(val) {
				console.log('多选', val)
				this.xzList = val

			},
			closes1() {
				console.log('-----1',this.couponIdList)
				for (var i in this.xzList) {
					this.couponIds.push(this.xzList[i].couponIssueId)
					this.couponIdList.push(this.xzList[i])
				}
				console.log('-----2',this.couponIdList)
				this.dialogFormVisible5 = false
			},

		},
		mounted() {
			this.dataSelect()
		}
	};
</script>

<style scoped>
.coupon-fk-page__filter-input {
	width: 160px;
}

.coupon-fk-page__filter-select {
	width: 150px;
}

.coupon-fk-page__filter-date {
	width: 160px;
}

.coupon-fk-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.coupon-fk-page__filter-actions-only .el-button + .el-button {
	margin-left: 12px;
}

.coupon-fk-page__tabs {
	margin-bottom: 0;
}

.coupon-fk-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.coupon-fk-page__tabs .nf-table-card {
	margin-bottom: 0;
}

.coupon-fk-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.coupon-fk-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.coupon-fk-page__link {
	color: #5cc477;
	cursor: pointer;
}

.coupon-fk-page__status-pending {
	color: #f56c6c;
}

.coupon-fk-page__status-used {
	color: #5cc477;
}

.coupon-fk-page__status-expired {
	color: #999;
}

.coupon-fk-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}
</style>
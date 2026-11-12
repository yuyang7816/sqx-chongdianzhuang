<template>
	<div class="nf-page finance-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">财务中心</span>
			<h2 class="nf-page-hero__title">财务中心</h2>
			<p class="nf-page-hero__desc">管理提现审核与转账、支付流水查询及提现/支付数据统计，支持多条件筛选与批量操作。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<el-tabs v-model="activeName" @tab-click="handleClick" class="finance-list-page__tabs">
		<el-tab-pane label="提现管理" name="first">
			<div class="nf-filter-card">
				<div class="nf-filter-card__head">
					<i class="el-icon-search nf-filter-card__head-icon"></i>
					<span>筛选条件</span>
				</div>
				<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
					<el-form-item label="支付宝账号">
						<el-input
							class="finance-list-page__filter-input"
							@keydown.enter.native="phoneSelect"
							clearable
							placeholder="请输入支付宝账号"
							v-model="zhifubao">
						</el-input>
					</el-form-item>
					<el-form-item label="支付宝名称">
						<el-input
							class="finance-list-page__filter-input"
							@keydown.enter.native="phoneSelect"
							clearable
							placeholder="请输入支付宝名称"
							v-model="zhifubaoName">
						</el-input>
					</el-form-item>
					<el-form-item label="提现类型">
						<el-select
							class="finance-list-page__filter-select"
							v-model="classify"
							placeholder="请选择提现类型"
							@change="phoneSelect(classify)">
							<el-option v-for="item in classifyList" :key="item.value" :label="item.label"
								:value="item.value">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="用户类型">
						<el-select
							class="finance-list-page__filter-select"
							v-model="userType"
							placeholder="请选择用户类型"
							@change="phoneSelect()">
							<el-option v-for="item in userTypeList" :key="item.value" :label="item.label"
								:value="item.value">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="状态">
						<el-select
							class="finance-list-page__filter-select"
							v-model="state1"
							placeholder="请选择状态"
							@change="phoneSelect(state)">
							<el-option v-for="item in states1" :key="item.value" :label="item.label" :value="item.value">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="开始时间">
						<el-date-picker
							class="finance-list-page__filter-date"
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
							class="finance-list-page__filter-date"
							v-model="endTime"
							align="right"
							type="datetime"
							format="yyyy-MM-dd"
							value-format="yyyy-MM-dd"
							placeholder="选择截止时间">
						</el-date-picker>
					</el-form-item>
					<el-form-item class="finance-list-page__filter-actions">
						<el-button type="primary" icon="el-icon-search" @click="phoneSelect">查询</el-button>
						<el-button type="primary" icon="el-icon-refresh-left" @click="cleans2">重置</el-button>
					</el-form-item>
				</el-form>
			</div>

			<div class="nf-table-card">
				<div class="finance-list-page__toolbar">
					<el-button type="warning" icon="el-icon-download" @click="exportBtn">导出Excel</el-button>
					<el-button type="primary" icon="el-icon-s-finance"
						@click="transferClcik()" :disabled="checkBoxData.length <= 0 || !isAuth('financeList:transfer')">
						批量转账
					</el-button>
					<el-button type="primary" icon="el-icon-s-finance"
						@click="transferClcikXx()" :disabled="checkBoxData.length <= 0 || !isAuth('financeList:transfer')">
						批量线下转账
					</el-button>
				</div>
				<div class="finance-list-page__notice finance-list-page__notice--danger">
					* 线下转账提示：支付宝线下转账成功核对状态后点击线下转账，请一定注意如果线下转账了不要再点击转账，否则造成损失自己承担
				</div>
				<div class="finance-list-page__notice finance-list-page__notice--warn">
					* 导出订单提示：导出订单前请进行时间或者状态等筛选，否则导出订单量过多易出现卡顿或系统崩溃
				</div>
			<el-table @selection-change="changeFun" v-loading="tableDataLoading" :data="tableData">
				<el-table-column type="selection">
				</el-table-column>
				<el-table-column fixed prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="zhifubao" label="支付宝账号" width="150">
					<template slot-scope="scope">
						<span class="finance-list-page__link"
							@click="updates(scope.row)">{{scope.row.zhifubao ? scope.row.zhifubao : '暂无'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="zhifubaoName" label="支付宝名称" width="100">
					<template slot-scope="scope">
						<span>{{scope.row.zhifubaoName ? scope.row.zhifubaoName : '暂无'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="cardFront" label="收款二维码" width="150">
					<template slot-scope="scope">
						<div v-if="scope.row.wxImg == null || scope.row.wxImg == ''">
							暂无图片
						</div>
						<div v-if="scope.row.wxImg" style="display:flex;flex-wrap: wrap;">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px" :src="scope.row.wxImg" alt="" slot="reference">
								<img style="width: 200px; height: 200px" :src="scope.row.wxImg" alt="">
							</el-popover>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="userType" label="用户类型" width="100">
					<template slot-scope="scope">
						<span v-if="scope.row.userType == 1">用户</span>
						<span v-else-if="scope.row.userType == 2">商家</span>
					</template>
				</el-table-column>
				<el-table-column prop="classify" label="提现方式" width="100">
					<template slot-scope="scope">
						<span v-if="scope.row.classify == 1">支付宝</span>
						<span v-else-if="scope.row.classify == 2">微信小程序</span>
						<span v-else-if="scope.row.classify == 3">微信公众号</span>
						<span v-else>暂无</span>
					</template>
				</el-table-column>
				<el-table-column prop="money" label="提现金额" width="100">
				</el-table-column>
				<el-table-column prop="state" label="状态" width="100">
					<template slot-scope="scope">
						<span class="finance-list-page__tag finance-list-page__tag--clickable" v-if="scope.row.state == 0">待转账</span>
						<span v-if="scope.row.state == 1">已转账</span>
						<span v-if="scope.row.state == -1">已拒绝</span>
					</template>
				</el-table-column>
				<el-table-column prop="refund" label="拒绝原因" width="220">
				</el-table-column>
				<el-table-column prop="createAt" label="申请时间" width="170">
				</el-table-column>
				<el-table-column prop="outAt" label="转账/拒绝时间" width="180">
				</el-table-column>
				<el-table-column prop="orderNumber" label="转账订单号" width="150">
				</el-table-column>
				<el-table-column fixed="right" label="操作" width="150">
					<template slot-scope="scope">
						<el-button size="mini" type="primary"
							:disabled="!isAuth('financeList:transfer') || scope.row.state != 0"
							@click="batch(scope.row)">转账
						</el-button>
						<el-button size="mini" type="primary"
							:disabled="!isAuth('financeList:refund') || scope.row.state != 0"
							@click="refund(scope.row)">拒绝
						</el-button>
						<el-button size="mini" type="primary"
							:disabled="!isAuth('financeList:transfer') || scope.row.state != 0"
							@click="batchXx(scope.row)" class="finance-list-page__op-btn">线下转账
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="finance-list-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="totalnum">
				</el-pagination>
			</div>
			</div>
		</el-tab-pane>
		<!-- <el-tab-pane label="收入统计" name="second">
			<div>
				<el-select v-model="flag" style="width:150px;margin-left: 10px;" @change="animeDat">
					<el-option v-for="item in flags" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;
				<el-date-picker style="width: 200px;margin-left: 10px;" v-model="info1.stockDate1" align="right"
					type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间"
					@change="animeSelect">
				</el-date-picker>
			</div>
			<el-row>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">发布任务支出金额</div>
							<div class="text_color"><span>{{MoneyData.sumPrice ? MoneyData.sumPrice : 0}}</span>元</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">用户收益</div>
							<div class="text_color">
								<span>{{MoneyData.sumMoneyBySend ? MoneyData.sumMoneyBySend : 0}}</span>元</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">平台任务收益</div>
							<div class="text_color">
								<span>{{MoneyData.sumMoneyByProfit ? MoneyData.sumMoneyByProfit  : 0}}</span>元</div>
						</div>
					</div>
				</el-col>
			</el-row>
		</el-tab-pane> -->
		<el-tab-pane label="提现统计" name="third">
			<div class="nf-filter-card">
				<div class="nf-filter-card__head">
					<i class="el-icon-date nf-filter-card__head-icon"></i>
					<span>筛选条件</span>
				</div>
				<div class="finance-list-page__stat-filter">
					<el-select v-model="flag" class="finance-list-page__filter-select" @change="animeDat2">
						<el-option v-for="item in flags" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
					<el-date-picker
						class="finance-list-page__filter-date finance-list-page__filter-date--wide"
						v-model="info1.stockDate1"
						align="right"
						type="date"
						format="yyyy-MM-dd"
						value-format="yyyy-MM-dd"
						placeholder="选择开始时间"
						@change="animeSelect2">
					</el-date-picker>
				</div>
			</div>
			<div class="finance-list-page__kpi-grid finance-list-page__kpi-grid--3">
				<div class="finance-list-page__kpi-card">
					<div class="finance-list-page__kpi-label">累计提现金额</div>
					<div class="finance-list-page__kpi-value">
						<span>{{ withdrawData.sumMoney }}</span>元
					</div>
				</div>
				<div class="finance-list-page__kpi-card">
					<div class="finance-list-page__kpi-label">提现次数</div>
					<div class="finance-list-page__kpi-value">
						<span>{{ withdrawData.countMoney }}</span>笔
					</div>
				</div>
				<div class="finance-list-page__kpi-card">
					<div class="finance-list-page__kpi-label">待转账次数</div>
					<div class="finance-list-page__kpi-value">
						<span>{{ withdrawData.stayMoney }}</span>笔
					</div>
				</div>
			</div>
		</el-tab-pane>
		<el-tab-pane label="支付统计" name="sixth">
			<div class="nf-filter-card">
				<div class="nf-filter-card__head">
					<i class="el-icon-date nf-filter-card__head-icon"></i>
					<span>筛选条件</span>
				</div>
				<div class="finance-list-page__stat-filter">
					<el-select v-model="flag" class="finance-list-page__filter-select" @change="animeDat3">
						<el-option v-for="item in flags" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
					<el-date-picker
						class="finance-list-page__filter-date finance-list-page__filter-date--wide"
						v-model="info1.stockDate1"
						align="right"
						type="date"
						format="yyyy-MM-dd"
						value-format="yyyy-MM-dd"
						placeholder="选择开始时间"
						@change="animeSelect3">
					</el-date-picker>
				</div>
			</div>
			<div class="finance-list-page__kpi-grid">
				<div class="finance-list-page__kpi-card">
					<div class="finance-list-page__kpi-label">支付总金额</div>
					<div class="finance-list-page__kpi-value">
						<span>{{ rechgeData.sumMoney ? rechgeData.sumMoney : 0 }}</span>元
					</div>
				</div>
				<div class="finance-list-page__kpi-card">
					<div class="finance-list-page__kpi-label">微信小程序支付金额</div>
					<div class="finance-list-page__kpi-value">
						<span>{{ rechgeData.weiXinXCXMoney ? rechgeData.weiXinXCXMoney : 0 }}</span>元
					</div>
				</div>
				<div class="finance-list-page__kpi-card">
					<div class="finance-list-page__kpi-label">微信APP支付金额</div>
					<div class="finance-list-page__kpi-value">
						<span>{{ rechgeData.weiXinAppMoney ? rechgeData.weiXinAppMoney : 0 }}</span>元
					</div>
				</div>
				<div class="finance-list-page__kpi-card">
					<div class="finance-list-page__kpi-label">微信公众号支付金额</div>
					<div class="finance-list-page__kpi-value">
						<span>{{ rechgeData.weiXinGZHMoney ? rechgeData.weiXinGZHMoney : 0 }}</span>元
					</div>
				</div>
				<div class="finance-list-page__kpi-card">
					<div class="finance-list-page__kpi-label">支付宝APP支付金额</div>
					<div class="finance-list-page__kpi-value">
						<span>{{ rechgeData.zhiFuBaoAppMoney ? rechgeData.zhiFuBaoAppMoney : 0 }}</span>元
					</div>
				</div>
				<div class="finance-list-page__kpi-card">
					<div class="finance-list-page__kpi-label">支付宝H5支付金额</div>
					<div class="finance-list-page__kpi-value">
						<span>{{ rechgeData.zhiFuBaoH5Money ? rechgeData.zhiFuBaoH5Money : 0 }}</span>元
					</div>
				</div>
				<div class="finance-list-page__kpi-card">
					<div class="finance-list-page__kpi-label">系统充值</div>
					<div class="finance-list-page__kpi-value">
						<span>{{ rechgeData.xiTongMoney ? rechgeData.xiTongMoney : 0 }}</span>元
					</div>
				</div>
				<div class="finance-list-page__kpi-card">
					<div class="finance-list-page__kpi-label">退款金额</div>
					<div class="finance-list-page__kpi-value">
						<span>{{ rechgeData.refundMoney ? rechgeData.refundMoney : 0 }}</span>元
					</div>
				</div>
			</div>
		</el-tab-pane>
		<el-tab-pane label="支付记录" name="fourth">
			<div class="nf-filter-card">
				<div class="nf-filter-card__head">
					<i class="el-icon-search nf-filter-card__head-icon"></i>
					<span>筛选条件</span>
				</div>
				<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
					<el-form-item label="支付类型">
						<el-select
							class="finance-list-page__filter-select"
							v-model="typeT"
							placeholder="请选择状态"
							@change="timeDate()">
							<el-option v-for="item in typeTs" :key="item.value" :label="item.label" :value="item.value">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="状态">
						<el-select
							class="finance-list-page__filter-select"
							v-model="state"
							placeholder="请选择状态"
							@change="timeDate()">
							<el-option v-for="item in states" :key="item.value" :label="item.label" :value="item.value">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="用户名称">
						<el-input
							class="finance-list-page__filter-input"
							@keydown.enter.native="timeDate"
							clearable
							placeholder="请输入用户名称"
							v-model="userName">
						</el-input>
					</el-form-item>
					<el-form-item label="手机号">
						<el-input
							class="finance-list-page__filter-input"
							@keydown.enter.native="timeDate"
							clearable
							placeholder="请输入手机号"
							v-model="phone">
						</el-input>
					</el-form-item>
					<el-form-item label="支付订单号">
						<el-input
							class="finance-list-page__filter-input"
							@keydown.enter.native="timeDate"
							clearable
							placeholder="请输入支付订单号"
							v-model="orderId">
						</el-input>
					</el-form-item>
					<el-form-item label="支付方式">
						<el-select
							class="finance-list-page__filter-select"
							v-model="classify"
							placeholder="请选择支付方式"
							@change="timeDate()">
							<el-option v-for="item in classifyList2" :key="item.value" :label="item.label" :value="item.value">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="开始时间">
						<el-date-picker
							class="finance-list-page__filter-date"
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
							class="finance-list-page__filter-date"
							v-model="endTime"
							align="right"
							type="date"
							format="yyyy-MM-dd"
							value-format="yyyy-MM-dd"
							placeholder="选择截止时间">
						</el-date-picker>
					</el-form-item>
					<el-form-item class="finance-list-page__filter-actions">
						<el-button type="primary" icon="el-icon-search" @click="timeDate">查询</el-button>
						<el-button type="primary" icon="el-icon-refresh-left" @click="clares">重置</el-button>
					</el-form-item>
				</el-form>
			</div>

			<div class="nf-table-card">
			<el-table v-loading="tableDataLoading" :data="rechargeData.list">
				<el-table-column fixed prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column label="支付类型">
					<template slot-scope="scope">
						<span class="finance-list-page__tag" v-if="scope.row.type == 1">会员卡充值</span>
						<span class="finance-list-page__tag" v-if="scope.row.type == 2">订单</span>
						<span class="finance-list-page__tag" v-if="scope.row.type == 3">预约</span>
						<span class="finance-list-page__tag" v-if="scope.row.type == 4">优惠券</span>
						<span class="finance-list-page__tag" v-if="scope.row.type == 5">结束</span>
						<span class="finance-list-page__tag" v-if="scope.row.type == 6">补费</span>
						<span class="finance-list-page__tag" v-if="scope.row.type == 9">续费</span>
					</template>
				</el-table-column>
				<el-table-column prop="userName" label="用户名称">
					<template slot-scope="scope">
						<span class="finance-list-page__link"
							@click="updates(scope.row)">{{scope.row.userName ? scope.row.userName : '未绑定'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="手机号" width="250">
				</el-table-column>
				<el-table-column prop="orderId" label="支付订单号" width="250">
				</el-table-column>
				<el-table-column prop="money" label="支付金额" width="100">
				</el-table-column>
				<el-table-column prop="refundMoney" label="退款金额" width="100">
				</el-table-column>
				<el-table-column label="支付方式" width="120">
					<template slot-scope="scope">
						<span class="finance-list-page__tag" v-if="scope.row.classify == 1">app微信</span>
						<span class="finance-list-page__tag" v-else-if="scope.row.classify == 2">微信公众号</span>
						<span class="finance-list-page__tag" v-else-if="scope.row.classify == 3">微信小程序</span>
						<span class="finance-list-page__tag" v-else-if="scope.row.classify == 4">支付宝</span>
						<span class="finance-list-page__tag" v-else-if="scope.row.classify == 5">支付宝H5</span>
						<span class="finance-list-page__tag" v-else-if="scope.row.classify == 6">系统充值</span>
						<span class="finance-list-page__tag" v-else>暂无</span>
					</template>
				</el-table-column>
				<el-table-column label="状态" width="120">
					<template slot-scope="scope">
						<span class="finance-list-page__tag" v-if="scope.row.state == 0">待支付</span>
						<span class="finance-list-page__tag" v-if="scope.row.state == 1">支付成功</span>
						<span class="finance-list-page__tag" v-if="scope.row.state == 2">支付失败</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="170">
				</el-table-column>
				<el-table-column prop="payTime" label="支付时间" width="170">
				</el-table-column>
				<el-table-column fixed="right" label="操作" width="100">
					<template slot-scope="scope">
						<el-button size="mini" type="primary"
							:disabled="!isAuth('financeList:transfer')&&!scope.row.refundMoney" v-if="scope.row.refundMoney<scope.row.money&&scope.row.state==1&&scope.row.classify != 6"
							@click="batchTk(scope.row)" class="finance-list-page__op-btn">退款
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="finance-list-page__pagination">
				<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
					:page-sizes="[5, 10, 15]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="rechargeData.totalCount">
				</el-pagination>
			</div>
			</div>
		</el-tab-pane>
		</el-tabs>

		<!-- 拒绝弹框 -->
		<el-dialog title="拒绝" :visible.sync="dialogFormVisible" center>
			<el-form :model="form">
				<el-form-item label="拒绝原因：" :label-width="formLabelWidth">
					<el-input v-model="form.content" type="textarea" rows="4" placeholder="请输入拒绝原因"
						style="width:65%;"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="refundNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 退款弹框 -->
		<el-dialog title="退款弹框" :visible.sync="dialogFormVisibleTk" center>
			<el-form :model="form">
				<el-form-item label="退款金额：" :label-width="formLabelWidth">
					<el-input v-model="form.content" type="text" placeholder="请输入退款金额" style="width:65%;"></el-input>
					<div class="finance-list-page__notice finance-list-page__notice--danger">* 当前可退款金额为：{{refundMoney}}</div>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleTk = false">取 消</el-button>
				<el-button type="primary" @click="tuikuanNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				page: 1,
				limit: 10,
				balance: -1,
				zhifubao: '',
				tkstatus: -1,
				state: 1,
				states: [{
						label: '全部',
						value: -1
					},
					{
						label: '待支付',
						value: 0
					},
					{
						label: '支付成功',
						value: 1
					},
					{
						label: '支付失败',
						value: 2
					}
				],
				state1: '',
				states1: [{
						label: '全部',
						value: ''
					},

					{
						label: '待提现',
						value: 0
					},
					{
						label: '提现成功',
						value: 1
					},
					{
						label: '拒绝提现',
						value: -1
					}
				],
				classify: '',
				classifyList: [{
						label: '全部',
						value: ''
					},
					{
						label: '支付宝',
						value: 1
					},
					{
						label: '微信',
						value: 2
					}
				],
				classifyList2: [{
						label: '全部',
						value: ''
					},
					{
						label: 'app微信',
						value: 1
					},
					{
						label: '微信公众号',
						value: 2
					},
					{
						label: '微信小程序',
						value: 3
					},
					{
						label: '支付宝',
						value: 4
					},
					{
						label: '支付宝H5',
						value: 5
					},
					{
						label: '系统充值',
						value: 6
					},
				],
				userType: '',
				userTypeList: [{
						label: '全部',
						value: ''
					},
					{
						label: '用户',
						value: 1
					},
					{
						label: '商家',
						value: 2
					}
				],
				zhifubaoName: '',
				content: '',
				startTime: '',
				endTime: '',
				cashId: '',
				type: -1,
				totalnum: 0,
				totalnum2: 0,
				sqxTotal: '',
				userTotal: '',
				taunTotal: '',
				relationId: '',
				sumMoneyByTime: '',
				money: '',
				activeName: 'first',
				formLabelWidth: '200px',
				dialogFormVisible: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				tableDataLoading: false,
				tableData: [],
				rechargeData: {},
				rechgeData: {},
				withdrawData: {},
				depositData: [],
				MoneyData: {},
				checkBoxData: [], //多选框选择的值
				flag: 1,
				form: {
					cashId: '',
					content: '',
				},
				form2: {
					id: '',
					money: '',
					createTime: ''
				},
				info: {
					stockDate: this.getNowTime(), //日期
				},
				info1: {
					stockDate1: this.getNowTime1(), //日期
				},
				info2: {
					stockDate2: this.getNowTime2(), //日期
				},
				flags: [{
					value: 1,
					label: '按天查询'
				}, {
					value: 2,
					label: '按月查询'
				}, {
					value: 3,
					label: '按年查询'
				}],
				userName: '',
				phone: '',
				orderId: '',
				typeT: '',
				typeTs: [{
						label: '全部',
						value: ''
					},
					{
						label: '会员卡充值',
						value: 1
					},
					{
						label: '订单',
						value: 2
					},
					{
						label: '预约',
						value: 3
					},
					{
						label: '优惠券',
						value: 4
					},
					{
						label: '结束',
						value: 5
					},
					{
						label: '补费',
						value: 6
					},
					{
						label: '续费',
						value: 9
					}
				],
				dialogFormVisibleTk: false,
				refundMoney: '',
				tkMoney: '',
				payDetailsId: '',
			}
		},
		methods: {
			// 会员详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
			// 批量转账
			transferClcik(id) {

				var ids = id ? [id] : this.checkBoxData.map(item => {
					return item.id
				})
				for (var i in ids) {
					this.$http({
						url: this.$http.adornUrl(`cash/alipay/${ids[i]}`),
						method: 'post',
						data: this.$http.adornData({})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$notify({
								title: '提示',
								duration: 1800,
								message: data.msg,
								type: 'warning'
							})
							return
						}
						if (data.code == 9999) {
							this.$notify({
								title: '提示',
								duration: 1800,
								message: data.msg,
								type: 'error'
							})
							return
						}
					})
				}
			},
			// 批量线下转账
			transferClcikXx(id) {
				var ids = id ? [id] : this.checkBoxData.map(item => {
					return item.id
				})
				console.log('ids', ids)
				this.$confirm(`确定批量线下转账?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('cash/outCash'),
						method: 'post',
						params: this.$http.adornParams({
							'cashOutIds': ids.toString()
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$notify({
								title: '提示',
								duration: 1800,
								message: '操作成功',
								type: 'warning'
							})
							this.dataSelect()
							return
						} else {
							this.$notify({
								title: '提示',
								duration: 1800,
								message: data.msg,
								type: 'error'
							})
						}
					})
				})
			},
			// 多选
			changeFun(val) {
				this.checkBoxData = val
			},
			//转账
			batch(row) {
				if (row.state == 1) {
					this.$message({
						message: '已转账，请勿重复操作！',
						type: 'error',
						duration: 1500
					})
					return
				}
				this.$confirm(`确定转账?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`cash/alipay/${row.id}`),
						method: 'post',
						data: this.$http.adornData({})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: data.msg,
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
							return
						}
						if (data.code == 9999) {
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
							return
						}
						if (data.code == 500) {
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
							return
						}
					})
				})
			},
			// 拒绝
			refund(row) {
				if (row.state == -1) {
					this.$message({
						message: '已拒绝，请勿重复操作！',
						type: 'error',
						duration: 1500
					})
				} else {
					this.dialogFormVisible = true
					this.form.cashId = row.id
				}
			},
			// 拒绝操作
			refundNoticeTo() {
				if (this.form.content == '') {
					this.$message({
						message: '请输入拒绝原因',
						type: 'error',
						duration: 1500
					})
					return
				}
				this.$http({
					url: this.$http.adornUrl(`cash/refund`),
					method: 'post',
					// data: this.$http.adornData({
					params: this.$http.adornParams({
						'cashId':this.form.cashId,
						'content':this.form.content,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.form.content = ''
								this.dataSelect()
							}
						})
						return
					}
					if (data.code == 500) {
						this.dialogFormVisible = false
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
							onClose: () => {
								this.form.content = ''
								this.dataSelect()
							}
						})
						return
					}
					if (data.code == -100) {
						this.dialogFormVisible = false
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
							onClose: () => {
								this.form.content = ''
								this.dataSelect()
							}
						})
						return
					}
				})
			},

			// 日期选择
			animeDat(value) {
				let vanumber = value
				if (vanumber === 1) {
					this.flag = 1
					this.incomeSelect()
				}
				if (vanumber === 2) {
					this.flag = 2
					this.incomeSelect()
				}
				if (vanumber === 3) {
					this.flag = 3
					this.incomeSelect()
				}
			},
			// 日期选择2
			animeDat2(value) {
				let vanumber = value
				if (vanumber === 1) {
					this.flag = 1
					this.withdrawSelect()
				}
				if (vanumber === 2) {

					this.flag = 2
					this.withdrawSelect()
				}
				if (vanumber === 3) {
					this.flag = 3
					this.withdrawSelect()
				}
			},
			// 日期选择3
			animeDat3(value) {
				let vanumber = value
				if (vanumber === 1) {
					this.flag = 1
					this.rechSelect()
				}
				if (vanumber === 2) {

					this.flag = 2
					this.rechSelect()
				}
				if (vanumber === 3) {
					this.flag = 3
					this.rechSelect()
				}
			},
			animeSelect() {
				this.incomeSelect()
			},
			animeSelect2() {
				this.withdrawSelect()
			},
			animeSelect3() {
				this.rechSelect()
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
			getNowTime2() {
				var now = new Date();
				var year = now.getFullYear(); //得到年份
				var month = now.getMonth(); //得到月份
				var date = now.getDate(); //得到日期
				month = month + 1;
				month = month.toString().padStart(2, "0");
				date = date.toString().padStart(2, "0");
				var defaultDate = `${year}-${month}-${date}`;
				return defaultDate;
				this.$set(this.info, "stockDate", defaultDate);
			},
			//处理默认选中当前日期
			getNowTime() {
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
			handleSizeChange(val) {
				this.limit = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			handleSizeChange1(val) {
				this.limit = val
				this.rechargeSelect()
			},
			handleCurrentChange1(val) {
				this.page = val
				this.rechargeSelect()
			},
			handleClick(tab, event) {
				if (tab._props.label == '提现管理') {
					this.page = 1
					this.limit = 10
					this.dataSelect()
				}
				if (tab._props.label == '收入统计') {
					this.flag = 1
					this.incomeSelect()
				}
				if (tab._props.label == '支付统计') {
					this.flag = 1
					this.rechSelect()
				}
				if (tab._props.label == '提现统计') {
					this.flag = 1
					this.withdrawSelect()
				}
				if (tab._props.label == '支付记录') {
					this.page = 1
					this.limit = 10

					this.userName = ''
					this.phone = ''
					this.orderId = ''
					this.typeT = ''
					this.rechargeSelect()
				}
			},
			timeDate() {
				this.page = 1
				this.rechargeSelect()
			},
			clares() {
				this.page = 1
				this.userName = ''
				this.phone = ''
				this.orderId = ''
				this.typeT = ''
				this.rechargeSelect()
			},
			// 查询
			phoneSelect() {
				this.page = 1
				this.dataSelect()
			},
			// 重置
			cleans2() {
				this.zhifubao = ''
				this.zhifubaoName = ''
				this.state1 = ''
				this.classify = ''
				this.userType = ''
				this.page = 1
				this.dataSelect()
			},
			// 支付宝账号查询
			//     zfbselect () {
			//       if (this.zhifubao == '') {
			//         this.$notify({
			//           title: '提示',
			//           duration: 1800,
			//           message: '请输入支付宝账号',
			//           type: 'error'
			//         })
			//         return
			//       }
			// this.page = 1
			//       this.tableDataLoading = true
			//       this.$http({
			//         url: this.$http.adornUrl('user/selectPayDetails'),
			//         method: 'get',
			//         params: this.$http.adornParams({
			// 	  'page':this.page,
			// 	  'limit':this.limit,
			// 	  'zhifubao':this.zhifubao
			//   })
			//       }).then(({data}) => {
			//         this.tableDataLoading = false
			//         let returnData = data.data
			//        this.tableData = returnData.list
			//        this.totalnum = returnData.totalCount
			//       })
			//     },
			//     // 支付宝名称
			//     qdselect () {
			//       if (this.zhifubaoName == '') {
			//         this.$notify({
			//           title: '提示',
			//           duration: 1800,
			//           message: '请输入渠道账号',
			//           type: 'error'
			//         })
			//         return
			//       }
			//       this.tableDataLoading = true
			//       this.$http({
			//         url: this.$http.adornUrl('user/selectPayDetails'),
			//         method: 'get',
			//         params: this.$http.adornParams({
			// 	  'page':this.page,
			// 	  'limit':this.limit,
			// 	  'zhifubaoName':this.zhifubaoName
			//   })
			//       }).then(({data}) => {
			//         this.tableDataLoading = false
			//         let returnData = data.data
			//         this.tableData = returnData.list
			//         this.totalnum = returnData.totalCount
			//       })
			//     },
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				var endTime = this.endTime
				if (this.endTime != '') {
					endTime = this.endTime + " 23:59:59"
				}
				this.$http({
					url: this.$http.adornUrl('cash/selectPayDetails'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'zhifubaoName': this.zhifubaoName,
						'zhifubao': this.zhifubao,
						'classify': this.classify,
						'state': this.state1,
						'startTime': this.startTime,
						'endTime': endTime,
						'userType': this.userType,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData = returnData.list
					this.totalnum = returnData.totalCount
				})
			},
			// 收入统计
			incomeSelect() {
				this.$http({
					url: this.$http.adornUrl('statistical/statisticsIncomeMoney'),
					method: 'get',
					params: this.$http.adornParams({
						'time': this.info1.stockDate1,
						'flag': this.flag
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						let returnData = data.data
						this.MoneyData = returnData
					}
				})
			},
			// 支付统计
			rechSelect() {
				this.$http({
					url: this.$http.adornUrl('cash/payMoney'),
					method: 'get',
					params: this.$http.adornParams({
						'time': this.info1.stockDate1,
						'flag': this.flag
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						let returnData = data.data
						this.rechgeData = returnData
					}
				})
			},
			// 提现统计
			withdrawSelect() {
				this.$http({
					url: this.$http.adornUrl('cash/statisticsCashMoney'),
					method: 'get',
					params: this.$http.adornParams({
						'time': this.info1.stockDate1,
						'flag': this.flag
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.withdrawData = returnData
				})
			},
			selectTrigger() {
				this.rechargeSelect()
			},
			// 获取支付记录
			rechargeSelect() {
				if (this.endTime == '') {
					this.endTime = this.info2.stockDate2
				}
				if (this.startTime == '') {
					this.startTime = this.info.stockDate
				}
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('cash/selectUserRecharge'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'state': this.state,
						'startTime': this.startTime,
						'endTime': this.endTime,
						'userName': this.userName,
						'phone': this.phone,
						'orderId': this.orderId,
						'type': this.typeT,


					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.rechargeData = returnData
					this.totalnum2 = returnData.totalCount
				})
			},
			// 线下转账
			batchXx(row) {
				if (row.state == 1) {
					this.$message({
						message: '已转账，请勿重复操作！',
						type: 'error',
						duration: 1500
					})
					return
				}
				this.$confirm(`确定线下转账?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('cash/outCash'),
						method: 'post',
						params: this.$http.adornParams({
							'cashOutIds': row.id
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$notify({
								title: '提示',
								duration: 1800,
								message: '操作成功',
								type: 'warning'
							})
							this.dataSelect()
							return
						} else {
							this.$notify({
								title: '提示',
								duration: 1800,
								message: data.msg,
								type: 'error'
							})
						}
					})
				})
			},
			// 导出
			exportBtn() {
				// if (this.endTime == '') {
				// 	this.endTime = this.info.stockDate
				// }
				// if (this.startTime == '') {
				// 	this.startTime = this.info2.stockDate2
				// }
				var endTime = this.endTime
				if (this.endTime != '') {
					endTime = this.endTime + " 23:59:59"
				}
				this.$http({
					url: this.$http.adornUrl('cash/excelPayDetails'),
					method: 'get',
					responseType: 'blob',
					params: this.$http.adornParams({
						// 'page': page,
						// 'size': this.size,
						'zhifubaoName': this.zhifubaoName,
						'zhifubao': this.zhifubao,
						'classify': this.classify,
						'state': this.state1,
						'startTime': this.startTime,
						'endTime': endTime,
						'userType': this.userType,
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
						elink.download = '财务列表.xlsx'
						elink.style.display = 'none'
						elink.href = url
						document.body.appendChild(elink)
						elink.click()
						document.body.removeChild(elink)
					}
				})
			},
			// 退款 弹框
			batchTk(row) {
				this.payDetailsId = row.id
				if(row.refundMoney){
					this.refundMoney = (Number(row.money)-Number(row.refundMoney)) 
				}else{
					this.refundMoney = (Number(row.money)) 
				}
				
				this.tkMoney = row.refundMoney
				this.form.content = row.money
				this.dialogFormVisibleTk = true
			},
			tuikuanNoticeTo() {
				if (Number(this.form.content) === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入退款金额',
						type: 'error'
					})
					return
				}
				if(Number(this.form.content)<=0||Number(this.form.content)>Number(this.refundMoney)){
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '退款金额必须大于0或者不可高于'+Number(this.refundMoney),
						type: 'error'
					})
					return
				}
				if(Number(this.form.content)>(Number(this.refundMoney))){
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '退款金额不可高于'+(Number(this.refundMoney)),
						type: 'error'
					})
					return
				}
				this.$confirm(`确定退款?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('cash/refundMoney'),
						method: 'post',
						params: this.$http.adornParams({
							'payDetailsId': this.payDetailsId,
							'refundMoney': this.form.content,
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.dialogFormVisibleTk = false
							this.$notify({
								title: '提示',
								duration: 1800,
								message: '操作成功',
								type: 'warning'
							})
							this.rechargeSelect()
							return
						} else {
							this.$notify({
								title: '提示',
								duration: 1800,
								message: data.msg,
								type: 'error'
							})
						}
					})
				})
			},
			dataSelect1() {
				 console.log('`````2```````',this.$route.query.status)
			  if(this.$route.query.status!=undefined&&this.$route.query.status){
			    this.state1 = Number(this.$route.query.status)
			  }else{
				 
			    this.state1 = ''
			  }
			  this.dataSelect()
			},
		},
		mounted() {
			 console.log('``1``````````',this.$route.query.status)
			if(this.$route.query.status!=undefined||this.$route.query.status===0){
			  this.state1 = Number(this.$route.query.status)
			}else{
			  this.state1 = ''
			}
			this.dataSelect()
		},
		watch: {
		  '$route': 'dataSelect1'
		}
	}
</script>

<style scoped>
.finance-list-page__filter-select {
	width: 150px;
}

.finance-list-page__filter-input {
	width: 200px;
}

.finance-list-page__filter-date {
	width: 160px;
}

.finance-list-page__filter-date--wide {
	width: 200px;
}

.finance-list-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.finance-list-page__stat-filter {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 12px;
}

.finance-list-page__tabs {
	margin-bottom: 0;
}

.finance-list-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.finance-list-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.finance-list-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.finance-list-page__tabs .nf-filter-card {
	margin-top: 12px;
}

.finance-list-page__tabs .nf-table-card,
.finance-list-page__tabs .finance-list-page__kpi-grid {
	margin-top: 12px;
	margin-bottom: 0;
}

.finance-list-page__toolbar {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 12px;
	margin-bottom: 12px;
}

.finance-list-page__notice {
	font-size: 13px;
	line-height: 1.5;
	margin-bottom: 8px;
}

.finance-list-page__notice--danger {
	color: #f56c6c;
}

.finance-list-page__notice--warn {
	color: #e6a23c;
}

.finance-list-page__link {
	color: #5cc477;
	cursor: pointer;
}

.finance-list-page__link:hover {
	color: #3fae5c;
}

.finance-list-page__tag {
	color: #5cc477;
}

.finance-list-page__tag--clickable {
	cursor: pointer;
}

.finance-list-page__op-btn {
	margin: 5px 0;
}

.finance-list-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.finance-list-page__kpi-grid {
	display: grid;
	grid-template-columns: repeat(4, 1fr);
	gap: 12px;
	background: #fff;
	border: 1px solid #e8eaed;
	border-radius: 8px;
	padding: 16px;
	box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}

.finance-list-page__kpi-grid--3 {
	grid-template-columns: repeat(3, 1fr);
}

.finance-list-page__kpi-card {
	padding: 16px;
	background: #f7f8fa;
	border: 1px solid #e8eaed;
	border-radius: 6px;
}

.finance-list-page__kpi-label {
	font-size: 13px;
	color: #909399;
	margin-bottom: 8px;
}

.finance-list-page__kpi-value {
	font-size: 14px;
	color: #303133;
}

.finance-list-page__kpi-value span {
	font-size: 22px;
	font-weight: 600;
	color: #5cc477;
	margin-right: 4px;
}

@media (max-width: 1200px) {
	.finance-list-page__kpi-grid,
	.finance-list-page__kpi-grid--3 {
		grid-template-columns: repeat(2, 1fr);
	}
}

@media (max-width: 768px) {
	.finance-list-page__kpi-grid,
	.finance-list-page__kpi-grid--3 {
		grid-template-columns: 1fr;
	}
}
</style>
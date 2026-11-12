<template>
	<div class="nf-page coupon-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">卡密管理</span>
			<h2 class="nf-page-hero__title">卡密管理</h2>
			<p class="nf-page-hero__desc">管理积分卡密，支持按卡号、是否绑定、是否生效筛选，可查询、重置、生成卡密、导出、批量修改与删除。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-search nf-filter-card__head-icon"></i>
				<span>筛选条件</span>
			</div>
			<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
				<el-form-item label="卡号">
					<el-input
						class="coupon-list-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入卡号"
						v-model="cardNo">
					</el-input>
				</el-form-item>
				<el-form-item label="是否绑定">
					<el-select
						class="coupon-list-page__filter-select"
						v-model="isActivate"
						placeholder="请选择是否绑定"
						@change="selectTrigger()">
						<el-option v-for="item in isActivates" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="是否生效">
					<el-select
						class="coupon-list-page__filter-select"
						v-model="cardIsActivation"
						placeholder="请选择是否生效"
						@change="selectTrigger()">
						<el-option v-for="item in cardIsActivations" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item class="coupon-list-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="select">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
					<el-button type="primary" icon="el-icon-plus" :disabled="!isAuth('couponList:add')" @click="addNotice">生成卡密</el-button>
					<el-button type="success" icon="el-icon-download" @click="derive">导出</el-button>
					<el-button type="primary" icon="el-icon-edit" :disabled="!isAuth('couponList:update') || checkBoxData.length <= 0" @click="batchAmend()">批量修改</el-button>
					<el-button type="danger" icon="el-icon-delete" :disabled="!isAuth('couponList:delete') || checkBoxData.length <= 0" @click="choideletes()">批量删除</el-button>
				</el-form-item>
			</el-form>
		</div>

		<el-tabs v-model="activeName" @tab-click="handleClick" class="coupon-list-page__tabs">
			<el-tab-pane label="卡密列表" name="first">
				<div class="nf-table-card">
				<el-table @selection-change="changeFun" v-loading="tableDataLoading" :data="tableData.content"
					row-key="id">
					<el-table-column type="selection" fixed>
					</el-table-column>
					<el-table-column prop="cardNo" label="卡号" width="120">
					</el-table-column>
					<el-table-column prop="cardPassword" label="卡密" width="150">
					</el-table-column>
					<el-table-column prop="money" label="积分数量">
					</el-table-column>
					<!-- <el-table-column prop="operatorUserName" label="关联运营商" width="150">
					</el-table-column> -->
					<el-table-column align="center" prop="isActivate" label="是否绑定">
						<template slot-scope="scope">
							<span class="coupon-list-page__status-active" v-if="scope.row.isActivate == 0">否</span>
							<span class="coupon-list-page__status-active" v-if="scope.row.isActivate == 1">是</span>
						</template>
					</el-table-column>
					<el-table-column prop="cardIsActivation" label="是否生效" align="center" width="120">
						<template slot-scope="scope">
							<span class="coupon-list-page__status-active" v-if="scope.row.cardIsActivation ==-1">否</span>
							<span class="coupon-list-page__status-active" v-if="scope.row.cardIsActivation == 0">是</span>
							<span class="coupon-list-page__status-active" v-if="scope.row.cardIsActivation == 1">是</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" header-align="center" align="center" label="创建时间" width="160">
					</el-table-column>
					<el-table-column fixed="right" label="操作" width="150">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="updates(scope.row)">修改
							</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('couponList:delete')"
								@click="deleteClick(scope.row)">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="coupon-list-page__pagination">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[10, 15, 20, 25]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tableData.totalElements">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="激活记录" name="third">
				<div class="nf-table-card">
				<div class="coupon-list-page__toolbar">
					<el-button type="primary" icon="el-icon-refresh" @click="refresh1">刷新</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="deductionData.content"
					row-key="id">
					<!-- <el-table-column type="selection">@selection-change="changeFun" 
					</el-table-column> -->
					<el-table-column prop="cardNo" label="卡号">
					</el-table-column>
					<!-- <el-table-column prop="operatorUserName" label="运营商">
					</el-table-column> -->
					<el-table-column prop="cardUserName" align="center" label="激活用户">
					</el-table-column>
					<el-table-column prop="exchangeTime" label="使用时间">
					</el-table-column>
					<el-table-column label="操作" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="danger" :disabled="!isAuth('couponList:delete')"
								@click="deleteClick1(scope.row)">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="coupon-list-page__pagination">
					<el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
						:page-sizes="[10, 15, 20, 25]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="deductionData.totalElements">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
		</el-tabs>

				<!-- 生成卡密-->
				<el-dialog title="生成卡密" :visible.sync="dialogFormVisible" center>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">卡密数量：</span>
						<el-input style="width:50%;" v-model="num" @input="changeInput" type="number"
							placeholder="请输入卡密数量"></el-input>
					</div>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">卡密积分：</span>
						<el-input style="width:50%;" v-model="money" @input="changeInput" type="number"
							placeholder="请输入卡密积分"></el-input>
					</div>
					<!-- <div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">关联运营商：</span>
						<el-select v-model="operatorUserId" placeholder="请选择运营商" style="width:50%;">
							<el-option v-for="item in operators" :key="item.id" :label="item.nickName" :value="item.id">
							</el-option>
						</el-select>
					</div> -->
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">是否生效：</span>
						<el-radio-group v-model="cardIsActivation">
							<el-radio :label="0">是</el-radio>
							<el-radio :label="-1">否</el-radio>
						</el-radio-group>
					</div>
					<div slot="footer" class="dialog-footer">
						<el-button @click="dialogFormVisible = false">取 消</el-button>
						<el-button type="primary" @click="releasNoticeTo()">确 定</el-button>
					</div>
				</el-dialog>
				<!-- 批量修改卡密 -->
				<el-dialog title="批量修改卡密" :visible.sync="dialogFormVisible1" center>
					<el-form :model="form">
						<el-form-item label="卡号：" :label-width="formLabelWidth">
							{{cardNofirst}} 至 {{cardNolast}}
						</el-form-item>
						<el-form-item label="卡密积分：" :label-width="formLabelWidth">
							<el-input style="width:50%;" v-model="form.money" @input="changeInput" type="number"
								placeholder="请输入卡密积分"></el-input>
						</el-form-item>
						<!-- <el-form-item label="关联运营商：" :label-width="formLabelWidth">
							<el-select v-model="form.operatorUserId" placeholder="请选择关联运营商" style="width:65%;">
								<el-option v-for="item in operators" :key="item.id" :label="item.nickName"
									:value="item.id">
								</el-option>
							</el-select>
						</el-form-item> -->
						<el-form-item label="是否生效：" :label-width="formLabelWidth">
							<el-radio-group v-model="form.cardIsActivation">
								<el-radio :label="0">是</el-radio>
								<el-radio :label="-1">否</el-radio>
							</el-radio-group>
						</el-form-item>
					</el-form>
					<div slot="footer" class="dialog-footer">
						<el-button @click="dialogFormVisible1 = false">取 消</el-button>
						<el-button type="primary" @click="batchNoticeTo()">确 定</el-button>
					</div>
				</el-dialog>
				<!-- 单个修改卡密 -->
				<el-dialog title="修改卡密" :visible.sync="dialogFormVisible3" center>
					<el-form :model="form1">
						<el-form-item label="卡号：" :label-width="formLabelWidth">
							<el-input v-model="form1.cardNo" style="width:65%;" placeholder="请输入卡号"></el-input>
						</el-form-item>
						<el-form-item label="卡密：" :label-width="formLabelWidth">
							<el-input v-model="form1.cardPassword" style="width:65%;" placeholder="请输入卡密"></el-input>
						</el-form-item>
						<el-form-item label="卡密积分：" :label-width="formLabelWidth">
							<el-input style="width:50%;" v-model="form1.money" @input="changeInput" type="number"
								placeholder="请输入卡密积分"></el-input>
						</el-form-item>
						<!-- <el-form-item label="关联运营商：" :label-width="formLabelWidth">
							<el-select v-model="form1.operatorUserId" placeholder="请选择关联运营商" style="width:65%">
								<el-option v-for="item in operators" :key="item.id" :label="item.nickName"
									:value="item.id">
								</el-option>
							</el-select>
						</el-form-item> -->
						<el-form-item label="是否生效：" :label-width="formLabelWidth">
							<el-radio-group v-model="form1.cardIsActivation">
								<el-radio :label="0">是</el-radio>
								<el-radio :label="-1">否</el-radio>
							</el-radio-group>
						</el-form-item>
					</el-form>
					<div slot="footer" class="dialog-footer">
						<el-button @click="dialogFormVisible3 = false">取 消</el-button>
						<el-button type="primary" @click="amendNoticeTo()">确 定</el-button>
					</div>
				</el-dialog>
				<!-- 导入-->
				<el-dialog title="导入选择" :visible.sync="dialogFormVisible4" center>
					<div v-loading="tableDataLoading2">
						<div style="margin-bottom: 10px;display:flex;">
							<span style="width: 200px;display: inline-block;text-align: right;">上传：</span>
							<el-upload class="avatar-uploader" v-model="fileUrl"
								:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
								:on-success="handleAvatarSuccess">
								<span v-if="excelname">{{excelname}}</span>
								<el-button v-else size="mini" type="primary" icon="document">文件上传</el-button>
							</el-upload>
						</div>
						<!-- <div style="margin-bottom: 10px;">
							<span style="width: 200px;display: inline-block;text-align: right;">关联运营商：</span>
							<el-select v-model="operatorUserId" placeholder="请选择关联运营商" style="width:50%;">
								<el-option v-for="item in operators" :key="item.id" :label="item.nickName"
									:value="item.id">
								</el-option>
							</el-select>
						</div> -->
						<div style="margin-bottom: 10px;">
							<span style="width: 200px;display: inline-block;text-align: right;">是否生效：</span>
							<el-radio-group v-model="cardIsActivation">
								<el-radio :label="0">是</el-radio>
								<el-radio :label="-1">否</el-radio>
							</el-radio-group>
						</div>
						<div slot="footer" class="dialog-footer" style="text-align: center;">
							<el-button @click="dialogFormVisible4 = false">取 消</el-button>
							<el-button type="primary" @click="channelNoticeTo()">确 定</el-button>
						</div>
					</div>
				</el-dialog>
				<!-- 导入重复-->
				<el-dialog title="导入失败" :visible.sync="dialogFormVisible5" center>
					<div style="margin-bottom: 10px;">
						<span
							style="width: 200px;display: inline-block;text-align:right;position: relative;top: -155px;">导入失败数据：</span>
						<el-input v-model="textmian" :value="textmian" style="width:65%;" type="textarea" :rows="8">
						</el-input>
					</div>
				</el-dialog>
			<!-- <el-tab-pane label="运营商列表" name="thirdly">
				<div style="margin:2% 0;display: inline-block;">
					<span>手机号：</span>
					<el-input style="width: 150px;" @keydown.enter.native="select2" clearable placeholder="请输入手机号"
						v-model="phone"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="display: inline-block;">
					<el-button style='margin-left:5px;' size="mini" type="primary" icon="document" @click="select2">查询
					</el-button>
					<el-button style='margin-left:5px;' size="mini" type="primary" icon="document" @click="cleans2">重置
					</el-button>
					<el-button style='margin-left:5px;' :disabled="!isAuth('couponList:add')" size="mini" type="primary"
						icon="document" @click="addOperator">新增运营商</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="operatorData.content" row-key="id">
					<el-table-column prop="nickName" label="运营商">
					</el-table-column>
					<el-table-column prop="phone" label="联系电话">
					</el-table-column>
					<el-table-column prop="invitationCount" align="center" label="下级会员数量">
					</el-table-column>
					<el-table-column prop="userType" label="类型">
						<template slot-scope="scope">
							<span v-if="scope.row.userType == 1">个人</span>
							<span v-if="scope.row.userType == 2">个体商户</span>
							<span v-if="scope.row.userType == 3">公司</span>
						</template>
					</el-table-column>
					<el-table-column prop="province" label="省">
					</el-table-column>
					<el-table-column prop="city" label="市">
					</el-table-column>
					<el-table-column prop="district" label="区">
					</el-table-column>
					<el-table-column prop="address" label="地址">
					</el-table-column>
					<el-table-column prop="endTime" label="到期时间">
					</el-table-column>
					<el-table-column label="操作" width="150">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="compile(scope.row)">编辑
							</el-button>
							<el-button size="mini" type="primary" @click="earning(scope.row)">收益
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
						:page-sizes="[10, 15, 20, 25]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="operatorData.totalElements">
					</el-pagination>
				</div>
			</el-tab-pane> -->
			<!-- 新增运营商-->
			<el-dialog title="新增运营商" :visible.sync="dialogFormVisible6" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">选择用户：</span>
					<el-input style="width:50%;" v-model="nickName" placeholder="请选择用户" @focus="userClick"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">类型：</span>
					<el-select v-model="userType" placeholder="请选择类型" style="width:50%;">
						<el-option v-for="item in userTypes" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">省市区：</span>
					<el-cascader style="width:50%" size="large" :options="options" ref="cascaderAddr"
						placeholder="请选择省市区" @change="handleChange">
					</el-cascader>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">详细地址：</span>
					<el-input style="width:50%;" v-model="address" placeholder="请输入详细地址"></el-input>
				</div>
				<div>
					<span style="width: 200px;display: inline-block;text-align: right;">到期时间：</span>
					<el-date-picker style="width: 50%;" v-model="info.stockDate" align="right" type="datetime"
						format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择到期时间">
					</el-date-picker>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible6 = false">取 消</el-button>
					<el-button type="primary" @click="operatorNotice()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改运营商-->
			<el-dialog title="修改运营商" :visible.sync="dialogFormVisible8" center>
				<el-form :model="form2">
					<el-form-item label="类型：" :label-width="formLabelWidth">
						<el-select v-model="form2.userType" placeholder="请选择类型" style="width:60%;">
							<el-option v-for="item in userTypes" :key="item.value" :label="item.label"
								:value="item.value">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="省市区：" :label-width="formLabelWidth" style="position: relative;">
						<span v-if="isshow"
							style="position:absolute;left:15px;top:2px;z-index: 99;background-color: #FFFFFF;">{{storeAddress}}</span>
						<el-cascader style="width:60%" size="large" v-model="addressone" :options="options"
							ref="cascaderAddr" @change="handleChange2">
						</el-cascader>
					</el-form-item>
					<el-form-item label="详细地址：" :label-width="formLabelWidth">
						<el-input style="width:60%;" v-model="form2.address" placeholder="请输入详细地址"></el-input>
					</el-form-item>
					<el-form-item label="到期时间：" :label-width="formLabelWidth">
						<el-date-picker style="width: 60%;" v-model="form2.month" align="right" type="datetime"
							format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择到期时间"
							@change="timeDate(form2.month)">
						</el-date-picker>
					</el-form-item>
				</el-form>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible8 = false">取 消</el-button>
					<el-button type="primary" @click="operatorNoticeto()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 用户列表-->
			<el-dialog title="用户列表" :visible.sync="dialogFormVisible7" center>
				<el-table v-loading="tableDataLoading" :data="userData.content">
					<el-table-column fixed prop="nickName" label="昵称" width="120">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{scope.row.nickName ? scope.row.nickName : '未设置'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号" width="120">
						<template slot-scope="scope">
							<span
								style="color: #5cc477;cursor: pointer;">{{scope.row.phone ? scope.row.phone : '未绑定'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间" width="180">
					</el-table-column>
					<el-table-column prop="endTime" label="会员到期时间" width="180">
					</el-table-column>
					<el-table-column prop="classify " label="会员开通方式" width="150">
						<template slot-scope="scope">
							<span v-if="scope.row.classify  == 1">微信支付</span>
							<span v-if="scope.row.classify  == 2">支付宝支付</span>
							<span v-if="scope.row.classify  == 3">卡密激活</span>
							<span v-if="scope.row.classify  == 4">管理平台开通</span>
						</template>
					</el-table-column>
					<el-table-column prop="gender" label="性别" width="100">
						<template slot-scope="scope">
							<span v-if="scope.row.gender == 1">男</span>
							<span v-if="scope.row.gender == 2">女</span>
							<span v-if="!scope.row.gender">未知</span>
						</template>
					</el-table-column>
					<el-table-column prop="relationId" label="会员编号" width="100">
						<template slot-scope="scope">
							<span>{{scope.row.isInvitation === 0 ? '未授权' : scope.row.relationId ? scope.row.relationId : '未绑定'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="invitation" label="邀请人" width="150">
						<template slot-scope="scope">
							<span style="color: #5cc477;cursor: pointer;"
								@click="colonelClick(scope.row.invitation)">{{scope.row.invitation ? scope.row.invitation : '未设置'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhifubao" label="支付宝">
						<template slot-scope="scope">
							<span>{{scope.row.zhifubaoName}}{{scope.row.zhifubao ? scope.row.zhifubao : '未绑定'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="jifen" label="余额" width="120">
						<template slot-scope="scope">
							<span>{{scope.row.jifen | numFilter}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="orderJifen" label="积分">
						<template slot-scope="scope">
							<span>{{scope.row.orderJifen | numFilter}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="name" label="备注" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.name}}</span>
							<div>
								<el-input type="text" @keydown.enter.native="remark(scope.row)" v-model="scope.row.name"
									clearable placeholder="备注" style="width: 120px;font-size:14px;">
									<i slot="suffix" class="el-input__icon el-icon-search"
										@click="remark(scope.row)"></i>
								</el-input>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="pub_share_pre_fee_user_s" label="来源">
						<template slot-scope="scope">
							<span v-if="scope.row.platform == 'weixin' ">公众号</span>
							<span v-if="scope.row.platform == 'wap' ">网站</span>
							<span v-if="scope.row.platform == 'app' ">APP</span>
							<span v-if="scope.row.platform == 'mp' ">小程序</span>
							<span v-if="scope.row.platform == '' "></span>
						</template>
					</el-table-column>
					<el-table-column label="操作" fixed='right' width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="addUser(scope.row)">添加
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[5, 10, 15, 20]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="userData.totalElements">
					</el-pagination>
				</div>
			</el-dialog>
			<!-- 收益 -->
			<el-dialog title="收益" :visible.sync="dialogFormVisible9" center>
				<el-table v-loading="tableDataLoading" :data="earningData.content">
					<el-table-column prop="id" label="编号" width="120">
					</el-table-column>
					<el-table-column prop="nickName" label="昵称">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{scope.row.nickName ? scope.row.nickName : '未设置'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="jifen" label="累计贡献" width="120">
						<template slot-scope="scope">
							<span>{{scope.row.jifen ? scope.row.jifen : 0}}元</span>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[5, 10, 15, 20]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="earningData.totalElements">
					</el-pagination>
				</div>
			</el-dialog>
	</div>
</template>

<script>
	import {
		provinceAndCityData,
		regionData,
		provinceAndCityDataPlus,
		regionDataPlus,
		CodeToText,
		TextToCode
	} from 'element-china-area-data'
	export default {
		data() {
			return {
				page: 1,
				size: 10,
				gradeIndex: '',
				num: '',
				userId: '',
				cardNo: '',
				cardIsActivation: '',
				cardIsActivations: [{
						label: '是',
						value: 0
					},
					{
						label: '否',
						value: -1
					}
				],
				isActivate: '',
				isActivates: [{
						label: '是',
						value: 1
					},
					{
						label: '否',
						value: 0
					}
				],
				userData: [],
				earningData: [],
				nickName: '',
				userId: '',
				address: '',
				storeAddress: '',
				province: '',
				city: '',
				district: '',
				options: regionData,
				userType: '',
				userTypes: [{
						label: '个人',
						value: 1
					},
					{
						label: '个体商户',
						value: 2
					},
					{
						label: '公司',
						value: 3
					}
				],
				form2: {
					id: '',
					userType: '',
					address: '',
					month: '',
				},
				isshow: false,
				userData: [],
				operatorUserId: '',
				uploadLoading: false,
				fileExcelList: [],
				operators: [],
				fileUrl: '',
				excelname: '',
				textmian: '',
				orderId: '',
				productId: '',
				couponAmount: '',
				activeName: 'first',
				checkBoxData: [], //多选框选择的值
				deductionData: [],
				form: {
					id: '',
					operatorUserId: '',
					cardIsActivation: '',
					money: '',
				},
				form1: {
					id: '',
					cardNo: '',
					money: '',
					isActivate: '',
					cardPassword: '',
					operatorUserId: '',
					cardIsActivation: '',
					createTime: ''
				},
				info: {
					stockDate: this.getNowTime(), //日期
				},
				money:'',
				phone: '',
				operatorData: [],
				fileList: [],
				file: '',
				cardNofirst: '',
				cardNolast: '',
				addressone: [],
				formLabelWidth: '200px',
				tableDataLoading2: false,
				tableDataLoading: false,
				dialogFormVisible1: false,
				dialogFormVisible: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				dialogFormVisible4: false,
				dialogFormVisible5: false,
				dialogFormVisible6: false,
				dialogFormVisible7: false,
				dialogFormVisible8: false,
				dialogFormVisible9: false,
				tableData: [],
				couponData: []
			}
		},
		filters: {
			numFilter(value) {
				let realVal = ''
				if (!isNaN(value) && value !== '') {
					// 截取当前数据到小数点后两位
					realVal = parseFloat(value).toFixed(2)
				} else {
					realVal = '--'
				}
				return realVal
			}
		},
		methods: {
			//处理默认选中当前日期
			getNowTime() {
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
			handleChange(value) {
				value = this.$refs['cascaderAddr'].currentLabels
				this.province = value[0]
				this.city = value[1]
				this.district = value[2]
			},
			handleChange2(value) {
				value = this.$refs['cascaderAddr'].currentLabels
				this.province = value[0]
				this.city = value[1]
				this.district = value[2]
				this.isshow = false
			},
			changeInput() {
				var pattern = /^[1-9][0-9]*$/ // 正整数的正则表达式
				// 不符合正整数时
				if (!pattern.test(this.num)) {
					// input 框绑定的内容为空
					this.num = ''
				}
			},
			// // 文件上传
			handleAvatarSuccess(file, fileList) {
				console.log(fileList)
				this.excelname = fileList.name
				this.fileUrl = file.data;
			},
			// 多选
			changeFun(val) {
				this.checkBoxData = val;
				if (val.length > 0) {
					this.cardNofirst = val[0].cardNo
					let index = parseInt(val.length) - 1
					this.cardNolast = val[index].cardNo
				}
			},
			handleSizeChange(val) {
				this.size = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			handleSizeChange2(val) {
				this.size = val
				this.deduction()
			},
			handleCurrentChange2(val) {
				this.page = val
				this.deduction()
			},
			handleSizeChange3(val) {
				this.size = val
				this.operatorSelect()
			},
			handleCurrentChange3(val) {
				this.page = val
				this.operatorSelect()
			},
			handleSizeChange4(val) {
				this.size = val
				this.earning()
			},
			handleCurrentChange4(val) {
				this.page = val
				this.earning()
			},
			// 刷新
			refresh1() {
				this.deduction()
			},
			// 查询
			select() {
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.cardNo = ''
				this.operatorUserId = ''
				this.cardIsActivation = ''
				this.isActivate = ''
				this.dataSelect()
			},
			handleClick(tab, event) {
				if (tab._props.label == '卡密列表') {
					this.page = 1
					this.size = 10
					this.dataSelect()
				}
				if (tab._props.label == '激活记录') {
					this.page = 1
					this.size = 10
					this.deduction()
				}
				if (tab._props.label == '运营商列表') {
					this.page = 1
					this.size = 10
					this.operatorSelect()
				}
				if (tab._props.label == '运营商收益') {
					this.page = 1
					this.size = 10
					this.operatorSelect()
				}
			},
			// 获取运营商
			// operator() {
			// 	this.gradeIndex = 2
			// 	this.$http({
			// 		url: this.$http.adornUrl2('/user/userListByGradeIndex'),
			// 		method: 'get',
			// 		params: this.$http.adornParams({
			// 			'gradeIndex': this.gradeIndex,
			// 		})
			// 	}).then(({
			// 		data
			// 	}) => {
			// 		this.tableDataLoading = false
			// 		let returnData = data.data;
			// 		this.operators = returnData
			// 	})
			// },
			// 导入
			channel() {
				this.fileUrl = ''
				this.operatorUserId == ''
				this.cardIsActivation == ''
				this.dialogFormVisible4 = true
			},
			// 导入excel文件
			channelNoticeTo() {
				if (this.fileUrl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择上传文件',
						type: 'warning'
					});
					return
				}

				// if (this.operatorUserId == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请选择运营商',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				if (this.cardIsActivation === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择是否生效',
						type: 'warning'
					});
					return
				}
				this.tableDataLoading2 = true
				this.$http({
					url: this.$http.adornUrl2(
						`/couponCard/import?operatorUserId=${this.operatorUserId}&cardIsActivation=${this.cardIsActivation}&fileUrl=${this.fileUrl}`
						),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					if (data.status == 0 && data.data.length > 0) {
						this.textmian = data.data
						this.dialogFormVisible4 = false
						this.dialogFormVisible5 = true
						this.tableDataLoading = false
					}
					if (data.status == 0 && data.data.length == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.operatorUserId == ''
								this.cardIsActivation == ''
								this.fileUrl = ''
								this.dialogFormVisible4 = false
								this.tableDataLoading2 = false
								this.dataSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.operatorUserId == ''
								this.cardIsActivation == ''
								this.fileUrl = ''
								this.dialogFormVisible4 = false
								this.tableDataLoading2 = false
								this.dataSelect()
							}
						})
					}
				})
			},
			// derive(){
			// 	this.dialogFormVisible2= true
			// },
			// 导出
			derive() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/couponCard/exportCouponCards'),
					method: 'post',
					responseType: 'blob',
					data: this.$http.adornData({
						'cardNo': this.cardNo,
						// 'operatorUserId': this.operatorUserId,
						'cardIsActivation': this.cardIsActivation
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let blob = new Blob([data], {
						type: 'application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
					})
					if (window.navigator.msSaveOrOpenBlob) {
						navigator.msSaveBlob(blob);
					} else {
						let url = window.URL.createObjectURL(blob)
						let elink = document.createElement('a');
						elink.download = "卡密列表.xlsx";
						elink.style.display = 'none';
						elink.href = url
						document.body.appendChild(elink);
						elink.click();
						document.body.removeChild(elink);
					}
				})
			},
			// 添加卡密弹框
			addNotice() {
				this.dialogFormVisible = true
			},
			// 添加卡密
			releasNoticeTo() {
				if (this.num == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入卡密数量',
						type: 'warning'
					});
					return
				}
				if (this.money == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入积分',
						type: 'warning'
					});
					return
				}
				if (this.cardIsActivation === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择是否生效',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl2('/couponCard/generateCouponCard'),
					method: 'get',
					params: this.$http.adornParams({
						'num': this.num,
						'money': this.money,
						'cardIsActivation': this.cardIsActivation,
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible = false
					this.$message({
						message: '添加成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.num = ''
							this.money = ''
							this.operatorUserId = ''
							this.cardIsActivation = ''
							this.dataSelect()
						}
					})
				})
			},
			// 批量修改卡密
			batchAmend() {
				this.dialogFormVisible1 = true
			},
			// 批量修改卡密
			batchNoticeTo(id) {
				if (this.form.money == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入卡密',
						type: 'warning'
					});
					return
				}
				if (this.form.cardIsActivation === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择是否生效',
						type: 'warning'
					});
					return
				}
				var idnum = id ? [id] : this.checkBoxData.map(item => {
					return item.id
				})
				let ids = idnum.join(',')
				this.$http({
					url: this.$http.adornUrl2(
						`/couponCard/updateCouponCardList/${ids}/${this.form.cardIsActivation}/${this.form.money}`
						),
					method: 'post',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					if (data.status == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dialogFormVisible1 = false
								this.dataSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 2500,
							onClose: () => {
								this.dialogFormVisible1 = false
								this.dataSelect()
							}
						})
					}
				})
			},
			// 单个修改卡密
			updates(row) {
				this.form1.id = row.id
				this.form1.cardNo = row.cardNo
				this.form1.money = row.money
				this.form1.isActivate = row.isActivate
				this.form1.cardPassword = row.cardPassword
				// this.form1.operatorUserId = row.operatorUserId
				this.form1.cardIsActivation = row.cardIsActivation
				this.form1.createTime = row.createTime
				this.dialogFormVisible3 = true
			},
			// 单个修改卡密
			amendNoticeTo() {
				if (this.form1.cardNo == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入卡号',
						type: 'warning'
					});
					return
				}
				if (this.form1.cardPassword === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入卡密',
						type: 'warning'
					});
					return
				}
				if (this.form1.money == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入积分',
						type: 'warning'
					});
					return
				}
				if (this.form1.cardIsActivation === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择是否生效',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl2('/couponCard/updateCouponCard'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.form1.id,
						'cardNo': this.form1.cardNo,
						'money': this.form1.money,
						'isActivate': this.form1.isActivate,
						'cardPassword': this.form1.cardPassword,
						// 'operatorUserId': this.form1.operatorUserId,
						'cardIsActivation': this.form1.cardIsActivation,
						'createTime': this.form1.createTime,
					})
				}).then(({
					data
				}) => {
					if (data.status == 0) {
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
							duration: 2500,
							onClose: () => {
								this.dialogFormVisible3 = false
								this.dataSelect()
							}
						})
					}
				})
			},
			// 批量删除卡密
			choideletes(id) {
				this.$confirm(`确定批量删除卡密?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					var idnum = id ? [id] : this.checkBoxData.map(item => {
						return item.id
					})
					let ids = idnum.join(',')
					this.$http({
						url: this.$http.adornUrl2(`/couponCard/deleteByIdList?ids=${ids}`),
						method: 'post',
						data: this.$http.adornData({})
					}).then(({
						data
					}) => {
						if (data.status == 0) {
							this.$message({
								message: '批量删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.page = 1
									this.size = 10
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
			// 删除卡密
			deleteClick(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl2(`/couponCard/deleteById?id=${row.id}`),
						method: 'post',
						data: this.$http.adornData({})
					}).then(({
						data
					}) => {
						if (data.status == 0) {
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
			// 批量删除抵扣记录
			choideletes1(id) {
				this.$confirm(`确定批量删除该信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					var idnum = id ? [id] : this.checkBoxData.map(item => {
						return item.id
					})
					let ids = idnum.join(',')
					this.$http({
						url: this.$http.adornUrl2(`/couponCard/deleteByIdList?ids=${ids}`),
						method: 'post',
						data: this.$http.adornData({})
					}).then(({
						data
					}) => {
						if (data.status == 0) {
							this.$message({
								message: '批量删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.page = 1
									this.size = 10
									this.deduction()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 1500,
								onClose: () => {
									this.deduction()
								}
							})
						}
					})
				}).catch(() => {})
			},
			// 删除激活记录
			deleteClick1(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl2(`/couponCard/deleteById?id=${row.id}`),
						method: 'post',
						data: this.$http.adornData({})
					}).then(({
						data
					}) => {
						if (data.status == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.deduction()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 1500,
								onClose: () => {
									this.deduction()
								}
							})
						}

					})
				}).catch(() => {})
			},
			// 卡密类型
			selectTrigger() {
				this.dataSelect()
			},
			// 获取卡密数据
			dataSelect() {
				let page = this.page - 1
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2(`/couponCard/findAllCouponCard?page=${page}&size=${this.size}`),
					method: 'get',
					params: this.$http.adornParams({
						'cardNo': this.cardNo,
						// 'operatorUserId': this.operatorUserId,
						'cardIsActivation': this.cardIsActivation,
						'isActivate': this.isActivate
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
				})
			},
			// 激活明细
			deduction() {
				let page = this.page - 1
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2(`/couponCard/selectCouponCardList?page=${page}&size=${this.size}`),
					method: 'get',
					params: this.$http.adornParams({
						'userId': this.userId,
						'orderId': this.orderId,
						'productId': this.productId,
						'couponAmount': this.couponAmount
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.deductionData = returnData
				})
			},
			// 收益
			earning(row) {
				this.dialogFormVisible9 = true
				let page = this.page - 1
				this.$http({
					url: this.$http.adornUrl2(`/user/team/users/${row.relationId}/1/${this.page-1}/${this.size}`),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.earningData = returnData
				})
			},
			// 查询
			select2() {
				this.operatorSelect()
			},
			//充值
			cleans2() {
				this.phone = ''
				this.operatorSelect()
			},
			// 新增运营商
			addOperator() {
				this.dialogFormVisible6 = true
			},
			// 新增运营商
			operatorNotice() {
				this.$http({
					url: this.$http.adornUrl2('/user/setUserMember'),
					method: 'get',
					params: this.$http.adornParams({
						'userId': this.userId,
						'address': this.address,
						'province': this.province,
						'city': this.city,
						'district': this.district,
						'userType': this.userType,
						'status': 2,
						'month': this.info.stockDate
					})
				}).then(({
					data
				}) => {
					this.$message({
						message: '添加成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.address = ''
							this.nickName = ''
							this.userType = ''
							this.dialogFormVisible6 = false
							this.operatorSelect()
						}
					})
				})
			},
			// 获取用户列表
			userClick() {
				let page = this.page - 1
				this.$http({
					url: this.$http.adornUrl2('/user/getUserByGradeIndexList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'size': this.size,
						'phone': this.phone
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.userData = returnData
					this.dialogFormVisible7 = true
				})
			},
			// 添加用户
			addUser(row) {
				this.userId = row.id
				this.nickName = row.nickName
				this.address = row.address
				this.dialogFormVisible7 = false
			},
			// 编辑运营商
			compile(row) {
				this.isshow = true
				this.form2.id = row.id
				this.form2.userType = row.userType
				this.form2.address = row.address
				this.form2.month = row.endTime
				this.province = row.province
				this.city = row.city
				this.district = row.district
				if (this.province == null) {
					this.province = ''
				}
				if (this.city == null) {
					this.city = ''
				}
				if (this.district == null) {
					this.district = ''
				}
				this.storeAddress = this.province + this.city + this.district
				this.dialogFormVisible8 = true
			},
			// 选择到期时间
			timeDate(data) {
				this.form2.month = data
			},
			// 编辑运营商
			operatorNoticeto() {
				this.$http({
					url: this.$http.adornUrl2('/user/updateOperatorUser'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.form2.id,
						'address': this.form2.address,
						'province': this.province,
						'city': this.city,
						'district': this.district,
						'userType': this.form2.userType,
						'status': 2,
						'month': this.form2.month
					})
				}).then(({
					data
				}) => {
					this.$message({
						message: '修改成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.address = ''
							this.nickName = ''
							this.userType = ''
							this.addressone = []
							this.dialogFormVisible8 = false
							this.operatorSelect()
						}
					})
				})
			},
			// 运营商列表
			operatorSelect() {
				this.tableDataLoading = true
				let page = this.page - 1
				this.$http({
					url: this.$http.adornUrl2('/user/operatorList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'size': this.size,
						'phone': this.phone
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.operatorData = returnData
				})
			}
		},
		mounted() {
			// this.operator()
			this.dataSelect()
		}
	};
</script>

<style scoped>
.coupon-list-page__filter-select {
	width: 150px;
}

.coupon-list-page__filter-input {
	width: 130px;
}

.coupon-list-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.coupon-list-page__tabs {
	margin-bottom: 0;
}

.coupon-list-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.coupon-list-page__tabs .nf-table-card {
	margin-bottom: 0;
}

.coupon-list-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.coupon-list-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.coupon-list-page__status-active {
	color: #5cc477;
}

.coupon-list-page__toolbar {
	display: flex;
	justify-content: flex-end;
	margin-bottom: 12px;
}

.coupon-list-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.nf-table-card .el-button + .el-button {
	margin-left: 0 !important;
	margin-top: 5px !important;
}
</style>

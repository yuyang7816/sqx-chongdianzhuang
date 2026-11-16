<template>
	<div class="nf-page user-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">用户管理</span>
			<h2 class="nf-page-hero__title">用户列表</h2>
			<p class="nf-page-hero__desc">管理平台注册用户，支持按昵称、手机号、邀请码及时间范围筛选，查看会员状态并进行详情、拉黑、赠送会员等操作。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<el-tabs v-model="activeName" @tab-click="handleClick" class="user-list-page__tabs">
			<el-tab-pane label="全部用户" name="first">
				<div class="nf-filter-card">
					<div class="nf-filter-card__head">
						<i class="el-icon-search nf-filter-card__head-icon"></i>
						<span>筛选条件</span>
					</div>
					<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
						<el-form-item label="用户昵称">
							<el-input class="user-list-page__filter-input" @keydown.enter.native="select" clearable
								placeholder="请输入用户昵称" v-model="userName"></el-input>
						</el-form-item>
						<el-form-item label="手机号">
							<el-input class="user-list-page__filter-input" @keydown.enter.native="select" clearable
								placeholder="请输入手机号" v-model="phone"></el-input>
						</el-form-item>
						<el-form-item label="邀请人邀请码">
							<el-input class="user-list-page__filter-input" @keydown.enter.native="select" clearable
								placeholder="请输入邀请人邀请码" v-model="inviterCode"></el-input>
						</el-form-item>
						<el-form-item label="开始时间">
							<el-date-picker class="user-list-page__filter-date" v-model="startTime" align="right"
								type="datetime" default-time="00:00:00" placeholder="选择开始时间">
							</el-date-picker>
						</el-form-item>
						<el-form-item label="截止时间">
							<el-date-picker class="user-list-page__filter-date" v-model="endTime" align="right"
								type="datetime" default-time="23:59:59" placeholder="选择截止时间">
							</el-date-picker>
						</el-form-item>
						<el-form-item class="user-list-page__filter-actions">
							<el-button type="primary" icon="el-icon-search" @click="select">查询</el-button>
							<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
							<el-button type="primary" icon="el-icon-plus" @click="addUserBtn()">新增用户</el-button>
						</el-form-item>
					</el-form>
				</div>
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData.list">
					<el-table-column fixed prop="userId" label="编号" width="80"></el-table-column>
					<el-table-column fixed prop="userName" label="昵称" width="150">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="isAuthentication" label="性别">
						<template slot-scope="scope">
							<span v-if="scope.row.sex==1">男</span>
							<span v-else-if="scope.row.sex==2">女</span>
							<span v-else>未设置</span>
						</template>
					</el-table-column>
					<el-table-column label="图像">
						<template slot-scope="scope">
							<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40"
								height="40">
							<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号">
						<template slot-scope="scope">
							<span class="user-list-page__link" @click="updates(scope.row)">
								{{ scope.row.phone ? scope.row.phone : '未绑定' }}
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="isVip" label="是否是会员">
						<template slot-scope="scope">
							<span v-if="scope.row.isVip==1">是</span>
							<span v-else>否</span>
						</template>
					</el-table-column>
					<el-table-column prop="vipEndTime" label="会员到期时间" width="160"></el-table-column>
					<el-table-column prop="invitationCode" label="邀请码"></el-table-column>
					<el-table-column label="邀请人邀请码">
						<template slot-scope="scope">
							<span class="user-list-page__link"
								@click="updates2(scope.row)">{{ scope.row.inviterCode ? scope.row.inviterCode : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiFuBaoName" label="支付宝名称">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBaoName ? scope.row.zhiFuBaoName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiFuBao" label="支付宝账号">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBao ? scope.row.zhiFuBao : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="wxImg" label="收款二维码" width="150">
						<template slot-scope="scope">
							<div v-if="scope.row.wxImg == null || scope.row.wxImg == ''">
								暂无收款二维码
							</div>
							<div v-else style="display:flex;flex-wrap: wrap;">
								<div style="margin: 2px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="scope.row.wxImg" alt=""
											slot="reference">
										<img style="width: 300px; height: 300px" :src="scope.row.wxImg" alt="">
									</el-popover>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
					<el-table-column prop="state " label="状态">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.status" @change="change(scope.row.status,scope.row.userId)"
								:active-value="openValue" :disabled="!isAuth('userList:delete')"
								:inactive-value="closeValue" active-color="#5cc477" inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<!-- <el-table-column prop="isRate" label="是否是推广用户" fixed="right">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.isRate" @change="changeTg(scope.row.isRate,scope.row.userId)"
								:active-value="openValue" :disabled="!isAuth('userList:delete')"
								:inactive-value="closeValue" active-color="#5cc477" inactive-color="#ff4949">
							</el-switch>
							<el-button size="mini" type="primary" v-if="scope.row.isRate==1" @click="erweima(scope.row)"
								style="margin: 5px;">查看二维码
							</el-button>
						</template>
					</el-table-column> -->
					<el-table-column fixed="right" label="操作" width="160">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="updates(scope.row)">用户详情</el-button>
							
							<el-button size="mini" type="danger" :disabled="!isAuth('userList:delete')"
								@click="deleteuser(scope.row)">删除用户</el-button>
							<el-button size="mini" type="primary" v-if="scope.row.status==1"
								@click="change(scope.row.status,scope.row.userId)">拉黑用户</el-button>
							<el-button size="mini" type="warning" @click="updateVip(scope.row)" >赠送会员</el-button>
							<el-button size="mini" type="warning" @click="quxiaoVip(scope.row)" v-if="scope.row.isVip==1" plain>取消会员</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="user-list-page__pagination">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[5, 10, 15, 20]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="会员用户" name="firstVip">
				<div class="nf-filter-card">
					<div class="nf-filter-card__head">
						<i class="el-icon-search nf-filter-card__head-icon"></i>
						<span>筛选条件</span>
					</div>
					<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
						<el-form-item label="用户昵称">
							<el-input class="user-list-page__filter-input" @keydown.enter.native="select" clearable
								placeholder="请输入用户昵称" v-model="userName"></el-input>
						</el-form-item>
						<el-form-item label="手机号">
							<el-input class="user-list-page__filter-input" @keydown.enter.native="select" clearable
								placeholder="请输入手机号" v-model="phone"></el-input>
						</el-form-item>
						<el-form-item label="邀请人邀请码">
							<el-input class="user-list-page__filter-input" @keydown.enter.native="select" clearable
								placeholder="请输入邀请人邀请码" v-model="inviterCode"></el-input>
						</el-form-item>
				<!-- <div style="margin:5px;display: inline-block;">
					<span>是否是推广用户：</span>
					<el-select v-model="isRateT" style="width:150px;margin-left: 10px;" @change="select()">
						<el-option v-for="item in typeNum2" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="margin:5px;display: inline-block;">
					<span>会员编号:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入会员编号"
						v-model="vipNo"></el-input>
				</div>&emsp;&emsp;
				<div style="margin:5px;display: inline-block;">
					<span>会员等级：</span>
					<el-select v-model="vipIndexT" style="width:150px;margin-left: 10px;" @change="select()">
						<el-option v-for="(item,index) in tableDataVip" :key="index" :value="item.vipIndex"
							:label="item.vipName">
						</el-option>
					</el-select>
				</div> -->
						<el-form-item label="开始时间">
							<el-date-picker class="user-list-page__filter-date" v-model="startTime" align="right"
								type="datetime" default-time="00:00:00" placeholder="选择开始时间">
							</el-date-picker>
						</el-form-item>
						<el-form-item label="截止时间">
							<el-date-picker class="user-list-page__filter-date" v-model="endTime" align="right"
								type="datetime" default-time="23:59:59" placeholder="选择截止时间">
							</el-date-picker>
						</el-form-item>
						<el-form-item class="user-list-page__filter-actions">
							<el-button type="primary" icon="el-icon-search" @click="select">查询</el-button>
							<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
							<el-button type="primary" icon="el-icon-plus" @click="addUserBtn()">新增用户</el-button>
						</el-form-item>
					</el-form>
				</div>
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData.list">
					<el-table-column fixed prop="userId" label="编号" width="80"></el-table-column>
					<el-table-column fixed prop="userName" label="昵称" width="150">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="isAuthentication" label="性别">
						<template slot-scope="scope">
							<span v-if="scope.row.sex==1">男</span>
							<span v-else-if="scope.row.sex==2">女</span>
							<span v-else>未设置</span>
						</template>
					</el-table-column>
					<el-table-column label="图像">
						<template slot-scope="scope">
							<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40"
								height="40">
							<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号">
						<template slot-scope="scope">
							<span class="user-list-page__link" @click="updates(scope.row)">
								{{ scope.row.phone ? scope.row.phone : '未绑定' }}
							</span>
						</template>
					</el-table-column>
					<!-- <el-table-column prop="vipIndex" label="会员等级" width="100">
						<template slot-scope="scope">
							<span style="color: #5cc477;cursor: pointer;"
								v-if="scope.row.vipIndex">{{scope.row.vipName}}</span>
							<span v-else>普通用户</span></br>
							<el-button size="mini" type="primary" @click="updatesVip(scope.row)" style="margin: 5px;">修改
							</el-button>
						</template>
					</el-table-column>
					<el-table-column prop="vipNo" label="会员编号" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.vipNo?scope.row.vipNo:''}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #5cc477;background: #fff;border: none;"
								@click="xiugai(scope.row,'vipNo')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="userVipDetailsCount" label="会员权益值" width="100"></el-table-column> -->
					
					<!-- <el-table-column prop="money" label="会员卡余额" width="80"></el-table-column>
					
					<el-table-column prop="zhiRate" label="一级佣金比例"  width="150">
						<template slot-scope="scope">
							<span>{{scope.row.zhiRate?scope.row.zhiRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #5cc477;background: #fff;border: none;"
								@click="xiugai(scope.row,'zhiRate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="feiRate" label="二级佣金比例"  width="150">
						<template slot-scope="scope">
							<span>{{scope.row.feiRate?scope.row.feiRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #5cc477;background: #fff;border: none;"
								@click="xiugai(scope.row,'feiRate')">修改</el-button>
							</span>
						</template>
					</el-table-column> -->

					<!-- <el-table-column prop="isAuthentication" label="是否实名">
						<template slot-scope="scope">
							<span v-if="scope.row.isAuthentication==1">已实名</span>
							<span v-else>未实名</span>
						</template>
					</el-table-column>
					 -->
					 <el-table-column prop="isVip" label="是否是会员">
					 	<template slot-scope="scope">
					 		<span v-if="scope.row.isVip==1">是</span>
					 		<span v-else>否</span>
					 	</template>
					 </el-table-column>
					 <el-table-column prop="vipEndTime" label="会员到期时间" width="160"></el-table-column>
					<el-table-column prop="invitationCode" label="邀请码"></el-table-column>
					<el-table-column label="邀请人邀请码">
						<template slot-scope="scope">
							<span class="user-list-page__link"
								@click="updates2(scope.row)">{{ scope.row.inviterCode ? scope.row.inviterCode : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiFuBaoName" label="支付宝名称">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBaoName ? scope.row.zhiFuBaoName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiFuBao" label="支付宝账号">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBao ? scope.row.zhiFuBao : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="wxImg" label="收款二维码" width="150">
						<template slot-scope="scope">
							<div v-if="scope.row.wxImg == null || scope.row.wxImg == ''">
								暂无收款二维码
							</div>
							<div v-else style="display:flex;flex-wrap: wrap;">
								<div style="margin: 2px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="scope.row.wxImg" alt=""
											slot="reference">
										<img style="width: 300px; height: 300px" :src="scope.row.wxImg" alt="">
									</el-popover>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
					<el-table-column prop="state " label="状态">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.status" @change="change(scope.row.status,scope.row.userId)"
								:active-value="openValue" :disabled="!isAuth('userList:delete')"
								:inactive-value="closeValue" active-color="#5cc477" inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<!-- <el-table-column prop="isRate" label="是否是推广用户" fixed="right">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.isRate" @change="changeTg(scope.row.isRate,scope.row.userId)"
								:active-value="openValue" :disabled="!isAuth('userList:delete')"
								:inactive-value="closeValue" active-color="#5cc477" inactive-color="#ff4949">
							</el-switch>
							<el-button size="mini" type="primary" v-if="scope.row.isRate==1" @click="erweima(scope.row)"
								style="margin: 5px;">查看二维码
							</el-button>
						</template>
					</el-table-column> -->
					<el-table-column fixed="right" label="操作" width="160">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="updates(scope.row)">用户详情</el-button>

							<el-button size="mini" type="danger" :disabled="!isAuth('userList:delete')"
								@click="deleteuser(scope.row)">删除用户</el-button>
							<el-button size="mini" type="primary" v-if="scope.row.status==1"
								@click="change(scope.row.status,scope.row.userId)">拉黑用户</el-button>
							<el-button size="mini" type="warning" @click="updateVip(scope.row)" >赠送会员</el-button>
							<el-button size="mini" type="warning" @click="quxiaoVip(scope.row)" v-if="scope.row.isVip==1" plain>取消会员</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="user-list-page__pagination">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[5, 10, 15, 20]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			
			<el-tab-pane label="商家用户" name="tuanzhang">
				<div class="nf-filter-card">
					<div class="nf-filter-card__head">
						<i class="el-icon-search nf-filter-card__head-icon"></i>
						<span>筛选条件</span>
					</div>
					<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
						<el-form-item label="用户昵称">
							<el-input class="user-list-page__filter-input" @keydown.enter.native="select" clearable
								placeholder="请输入用户昵称" v-model="userName"></el-input>
						</el-form-item>
						<el-form-item label="手机号">
							<el-input class="user-list-page__filter-input" @keydown.enter.native="select" clearable
								placeholder="请输入手机号" v-model="phone"></el-input>
						</el-form-item>
						<el-form-item label="邀请人邀请码">
							<el-input class="user-list-page__filter-input" @keydown.enter.native="select" clearable
								placeholder="请输入邀请人邀请码" v-model="inviterCode"></el-input>
						</el-form-item>
						<el-form-item label="开始时间">
							<el-date-picker class="user-list-page__filter-date" v-model="startTime" align="right"
								type="datetime" default-time="00:00:00" placeholder="选择开始时间">
							</el-date-picker>
						</el-form-item>
						<el-form-item label="截止时间">
							<el-date-picker class="user-list-page__filter-date" v-model="endTime" align="right"
								type="datetime" default-time="23:59:59" placeholder="选择截止时间">
							</el-date-picker>
						</el-form-item>
						<el-form-item class="user-list-page__filter-actions">
							<el-button type="primary" icon="el-icon-search" @click="select">查询</el-button>
							<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
						</el-form-item>
					</el-form>
				</div>
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData.list">
					<el-table-column fixed prop="userId" label="编号" width="80"></el-table-column>
					<el-table-column fixed prop="userName" label="昵称" width="150">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="sex" label="性别">
						<template slot-scope="scope">
							<span v-if="scope.row.sex==1">男</span>
							<span v-else-if="scope.row.sex==2">女</span>
							<span v-else>未设置</span>
						</template>
					</el-table-column>
					<el-table-column label="图像">
						<template slot-scope="scope">
							<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40"
								height="40">
							<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号" width="150">
						<template slot-scope="scope">
							<span class="user-list-page__link" @click="updates(scope.row)">
								{{ scope.row.phone ? scope.row.phone : '未绑定' }}
							</span>
						</template>
					</el-table-column>
					<!-- <el-table-column prop="vipNo" label="会员编号" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.vipNo?scope.row.vipNo:''}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #5cc477;background: #fff;border: none;"
								@click="xiugai(scope.row,'vipNo')">修改</el-button>
							</span>
						</template>
					</el-table-column> -->
					<!-- <el-table-column prop="money" label="会员卡余额" width="80"></el-table-column>
					<el-table-column prop="zhiRate" label="一级佣金比例"  width="150">
						<template slot-scope="scope">
							<span>{{scope.row.zhiRate?scope.row.zhiRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #5cc477;background: #fff;border: none;"
								@click="xiugai(scope.row,'zhiRate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="feiRate" label="二级佣金比例"  width="150">
						<template slot-scope="scope">
							<span>{{scope.row.feiRate?scope.row.feiRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #5cc477;background: #fff;border: none;"
								@click="xiugai(scope.row,'feiRate')">修改</el-button>
							</span>
						</template>
					</el-table-column> -->
					<!-- <el-table-column prop="isAuthentication" label="是否实名">
						<template slot-scope="scope">
							<span v-if="scope.row.isAuthentication==1">已实名</span>
							<span v-else>未实名</span>
						</template>
					</el-table-column> -->
					<el-table-column prop="isVip" label="是否是会员">
						<template slot-scope="scope">
							<span v-if="scope.row.isVip==1">是</span>
							<span v-else>否</span>
						</template>
					</el-table-column>
					<el-table-column prop="vipEndTime" label="会员到期时间" width="160"></el-table-column>
					<el-table-column prop="invitationCode" label="邀请码"></el-table-column>
					<el-table-column label="邀请人邀请码">
						<template slot-scope="scope">
							<el-button size="mini" style="color: #5cc477;background: #fff;border: none;" type="primary"
								@click="updates2(scope.row)">
								{{scope.row.inviterCode ? scope.row.inviterCode : '未绑定'}}
							</el-button>
						</template>
					</el-table-column>
					<el-table-column prop="zhiFuBaoName" label="支付宝名称">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBaoName ? scope.row.zhiFuBaoName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiFuBao" label="支付宝账号">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBao ? scope.row.zhiFuBao : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="wxImg" label="收款二维码" width="150">
						<template slot-scope="scope">
							<div v-if="scope.row.wxImg == null || scope.row.wxImg == ''">
								暂无收款二维码
							</div>
							<div v-else style="display:flex;flex-wrap: wrap;">
								<div style="margin: 2px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="scope.row.wxImg" alt=""
											slot="reference">
										<img style="width: 300px; height: 300px" :src="scope.row.wxImg" alt="">
									</el-popover>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
					<el-table-column prop="state " label="状态">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.status" @change="change(scope.row.status,scope.row.userId)"
								:active-value="openValue" :disabled="!isAuth('userList:delete')"
								:inactive-value="closeValue" active-color="#5cc477" inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<!-- <el-table-column prop="isRate" label="是否是推广用户" fixed="right">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.isRate" @change="changeTg(scope.row.isRate,scope.row.userId)"
								:active-value="openValue" :disabled="!isAuth('userList:delete')"
								:inactive-value="closeValue" active-color="#5cc477" inactive-color="#ff4949">
							</el-switch>
							<el-button size="mini" type="primary" v-if="scope.row.isRate==1" @click="erweima(scope.row)"
								style="margin: 5px;">查看二维码
							</el-button>
						</template>
					</el-table-column> -->
					<el-table-column fixed="right" label="操作" width="180">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="updates(scope.row)">用户详情</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('userList:delete')"
								@click="deleteuser(scope.row)">删除用户</el-button>
							<el-button size="mini" type="primary" v-if="scope.row.status==1"
								@click="change(scope.row.status,scope.row.userId)">拉黑用户</el-button>
							<el-button size="mini" type="warning" @click="updateVip(scope.row)" >赠送会员</el-button>
							<el-button size="mini" type="warning" @click="quxiaoVip(scope.row)" v-if="scope.row.isVip==1" plain>取消会员</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="user-list-page__pagination">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[5, 10, 15, 20]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="黑名单用户" name="heimingdan">
				<div class="nf-filter-card">
					<div class="nf-filter-card__head">
						<i class="el-icon-search nf-filter-card__head-icon"></i>
						<span>筛选条件</span>
					</div>
					<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
						<el-form-item label="用户昵称">
							<el-input class="user-list-page__filter-input" @keydown.enter.native="select" clearable
								placeholder="请输入用户昵称" v-model="userName"></el-input>
						</el-form-item>
						<el-form-item label="手机号">
							<el-input class="user-list-page__filter-input" @keydown.enter.native="select" clearable
								placeholder="请输入手机号" v-model="phone"></el-input>
						</el-form-item>
						<el-form-item label="邀请人邀请码">
							<el-input class="user-list-page__filter-input" @keydown.enter.native="select" clearable
								placeholder="请输入邀请人邀请码" v-model="inviterCode"></el-input>
						</el-form-item>
						<el-form-item label="是否是推广用户">
							<el-select class="user-list-page__filter-select" v-model="isRateT" @change="select()">
								<el-option v-for="item in typeNum2" :key="item.value" :label="item.label" :value="item.value">
								</el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="会员编号">
							<el-input class="user-list-page__filter-input" @keydown.enter.native="select" clearable
								placeholder="请输入会员编号" v-model="vipNo"></el-input>
						</el-form-item>
						<el-form-item label="开始时间">
							<el-date-picker class="user-list-page__filter-date" v-model="startTime" align="right"
								type="datetime" default-time="00:00:00" placeholder="选择开始时间">
							</el-date-picker>
						</el-form-item>
						<el-form-item label="截止时间">
							<el-date-picker class="user-list-page__filter-date" v-model="endTime" align="right"
								type="datetime" default-time="23:59:59" placeholder="选择截止时间">
							</el-date-picker>
						</el-form-item>
						<el-form-item class="user-list-page__filter-actions">
							<el-button type="primary" icon="el-icon-search" @click="select">查询</el-button>
							<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
						</el-form-item>
					</el-form>
				</div>
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData.list">
					<el-table-column fixed prop="userId" label="编号" width="80"></el-table-column>
					<el-table-column fixed prop="userName" label="昵称" width="150">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="isAuthentication" label="性别">
						<template slot-scope="scope">
							<span v-if="scope.row.sex==1">男</span>
							<span v-else-if="scope.row.sex==2">女</span>
							<span v-else>未设置</span>
						</template>
					</el-table-column>
					<el-table-column label="图像">
						<template slot-scope="scope">
							<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40"
								height="40">
							<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号">
						<template slot-scope="scope">
							<span class="user-list-page__link" @click="updates(scope.row)">
								{{ scope.row.phone ? scope.row.phone : '未绑定' }}
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="vipNo" label="会员编号" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.vipNo?scope.row.vipNo:''}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #5cc477;background: #fff;border: none;"
								@click="xiugai(scope.row,'vipNo')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="isVip" label="是否是会员">
						<template slot-scope="scope">
							<span v-if="scope.row.isVip==1">是</span>
							<span v-else>否</span>
						</template>
					</el-table-column>
					<el-table-column prop="vipEndTime" label="会员到期时间" width="160"></el-table-column>
					<el-table-column prop="invitationCode" label="邀请码"></el-table-column>
					<el-table-column label="邀请人邀请码">
						<template slot-scope="scope">
							<span class="user-list-page__link"
								@click="updates2(scope.row)">{{ scope.row.inviterCode ? scope.row.inviterCode : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiFuBaoName" label="支付宝名称">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBaoName ? scope.row.zhiFuBaoName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiFuBao" label="支付宝账号">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBao ? scope.row.zhiFuBao : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="wxImg" label="收款二维码" width="150">
						<template slot-scope="scope">
							<div v-if="scope.row.wxImg == null || scope.row.wxImg == ''">
								暂无收款二维码
							</div>
							<div v-else style="display:flex;flex-wrap: wrap;">
								<div style="margin: 2px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="scope.row.wxImg" alt=""
											slot="reference">
										<img style="width: 300px; height: 300px" :src="scope.row.wxImg" alt="">
									</el-popover>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
					<el-table-column prop="state " label="状态">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.status" @change="change(scope.row.status,scope.row.userId)"
								:active-value="openValue" :disabled="!isAuth('userList:delete')"
								:inactive-value="closeValue" active-color="#5cc477" inactive-color="#ff4949">
							</el-switch>
							<div v-if="scope.row.status==1" style="color: #5cc477;">正常</div>
							<div v-if="scope.row.status==2" style="color: #ff4949;">封禁</div>
						</template>
					</el-table-column>
					<el-table-column prop="isRate" label="是否是推广用户" fixed="right">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.isRate" @change="changeTg(scope.row.isRate,scope.row.userId)"
								:active-value="openValue" :disabled="!isAuth('userList:delete')"
								:inactive-value="closeValue" active-color="#5cc477" inactive-color="#ff4949">
							</el-switch>
							<el-button size="mini" type="primary" v-if="scope.row.isRate==1" @click="erweima(scope.row)"
								style="margin: 5px;">查看二维码
							</el-button>
						</template>
					</el-table-column>
					<el-table-column fixed="right" label="操作" width="180">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="updates(scope.row)">用户详情</el-button>

							<el-button size="mini" type="danger" :disabled="!isAuth('userList:delete')"
								@click="deleteuser(scope.row)">删除用户</el-button>
							<el-button size="mini" type="warning" plain v-if="scope.row.status==2"
								@click="change(scope.row.status,scope.row.userId)">解除黑名单</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="user-list-page__pagination">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[5, 10, 15, 20]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<!-- 修改比例 -->
			<el-dialog :title="titleBl" :visible.sync="dialogFormVisible2" center>
				<div style="margin-bottom: 10px;" v-if="titleBl=='会员编号'">
					<span style="width: 200px;display: inline-block;text-align: right;">会员编号：</span>
					<el-input style="width:50%;" v-model="proportion" type="text" :controls="false"
						:placeholder="titleBl"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-else>
					<span style="width: 200px;display: inline-block;text-align: right;">比例：</span>
					<el-input style="width:50%;" v-model="proportion" type="number" :min="0" :controls="false"
						:placeholder="titleBl"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible2 = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeTo2()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 推广二维码 -->
			<el-dialog :visible.sync="dialogFormVisibleEwm" center title="推广二维码">
				<div>
					<img :src="erweimaImg" style="width: 50%;margin-left: 25%;" />
					<div style="text-align: center;margin-top: 20px;">
						<el-button type="primary" @click="chakan()">打开窗口查看</el-button>
						<el-button type="primary" @click="downloadImage()"><a style="color: #fff;">下载</a></el-button>
					</div>

				</div>
			</el-dialog>
			<!-- 修改会员 -->
			<el-dialog title="修改会员" :visible.sync="dialogFormVisible3" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">会员等级：</span>
					<el-select v-model="vipIndex" style="width:200px;margin-left: 10px;">
						<el-option v-for="(item,index) in tableDataVip" :key="index" :value="item.vipIndex"
							:label="item.vipName">
							<!-- 等级{{item.vipIndex}}:{{item.vipName}} -->
						</el-option>
					</el-select>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible3 = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeTo3()" v-if="!Loading">确 定</el-button>
					<el-button type="primary" v-else v-loading="Loading">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 新增用户 -->
			<el-dialog title="新增用户" :visible.sync="dialogFormVisible4" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">手机号：</span>
					<el-input style="width:50%;" v-model="phoneAdd" type="text" :controls="false"
						placeholder="请输入手机号"></el-input>
				</div>
				<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">会员等级：</span>
					<el-select v-model="vipIndex" style="width:50%;">
						<el-option v-for="(item,index) in tableDataVip" :key="index" :value="item.vipIndex" :label="item.vipName">
						</el-option>
					</el-select>
				</div> -->
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible4 = false">取 消</el-button>
					<el-button type="primary" @click="addNoticeTo()" v-if="!Loading">确 定</el-button>
					<el-button type="primary" v-else v-loading="Loading">确 定</el-button>
				</div>
			</el-dialog>
		</el-tabs>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				openValue: 1,
				closeValue: 2,
				state: 'false',
				limit: 10,
				page: 1,
				userName: '',
				inviterCode: '',
				isAuthentication: '',
				nickNam: '',
				phone: '',
				platform: 'all',
				isRelation: -1,
				isTuan: -1,
				value: '',
				member: -1,
				payData: [],
				nickName: '',
				grade: '',
				name: '',
				activeName: 'first',
				tableDataLoading: true,
				dialogFormVisible2: false,
				tableData: [],
				platforms: [{
						value: 'all',
						label: '全部'
					}, {
						value: 'wap',
						label: '网站'
					}, {
						value: 'app',
						label: 'APP'
					}, {
						value: 'weixin',
						label: '微信公众号'
					},
					{
						value: 'mp',
						label: '微信小程序'
					}
				],
				campus: '',
				homeData1: [],
				startTime: '',
				endTime: '',
				info: {
					stockDate: this.getNowTime(), //日期
				},
				info1: {
					stockDate1: this.getNowTime1(), //日期
				},
				info2: {
					stockDate2: this.getNowTime2(), //日期
				},
				proportion: '',
				texts: '',
				userIdss: '',
				titleBl: '修改佣金比例',
				invitationCode: '',
				statusT: '',
				isRateT: '',
				vipIndexT: '',
				vipNo: '',
				typeNum2: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '是'
					},
					{
						value: 2,
						label: '否'
					},
				],
				dialogFormVisibleEwm: false,
				erweimaImg: '',
				erweimaImgName: '',
				isVip: '',
				dialogFormVisible3: false,
				tableDataVip: [],
				vipIndex: '',
				moneyUser: '',
				dialogFormVisible4: false,
				phoneAdd: '',
				Loading: false,
				birthdayK:'',
				birthdayJ:'',
				isBirthday:'',
				birthdayMonth:'',
				integralNum:'',
				userVipDetails:'',

			}
		},
		methods: {
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
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() - now.getMonth() //得到月份
				var date = now.getDate() - now.getDate() + 1 //得到日期
				var hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
				var mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
				var ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
			},
			// tabs切换
			handleClick(tab, event) {
				this.page = 1
				this.limit = 10
				this.startTime = ''
				this.endTime = ''
				this.isRateT = ''
				this.vipNo = ''
				this.phone = ''
				this.vipIndexT = ''
				this.birthdayMonth = ''
				if (tab._props.label == '全部用户') {
					this.isAuthentication = ''
					this.statusT = ''
					this.isVip = ''
					this.moneyUser = ''
					this.isBirthday = ''
					this.integralNum = ''
					this.userVipDetails = ''
					this.dataSelect()
				}
				if (tab._props.label == '会员用户') {
					this.isAuthentication = ''
					this.statusT = ''
					this.isVip = 1
					this.moneyUser = ''
					this.isBirthday = ''
					this.integralNum = ''
					this.userVipDetails = 1
					this.dataSelect()
				}
				if (tab._props.label == '生日用户') {
					this.isAuthentication = ''
					this.statusT = ''
					this.isVip = ''
					this.moneyUser = ''
					this.isBirthday = 1
					this.integralNum = ''
					this.userVipDetails = ''
					this.dataSelect()
				}
				if (tab._props.label == '余额用户') {
					this.isAuthentication = ''
					this.statusT = ''
					this.isVip = ''
					this.isBirthday = ''
					this.moneyUser = 1
					this.integralNum = ''
					this.userVipDetails = ''
					this.dataSelect()
				}
				if (tab._props.label == '积分用户') {
					this.isAuthentication = ''
					this.statusT = ''
					this.isVip = ''
					this.isBirthday = ''
					this.moneyUser =''
					this.integralNum = 1
					this.userVipDetails = ''
					this.dataSelect()
				}
				

				if (tab._props.label == '商家用户') {
					this.isAuthentication = 3
					this.statusT = ''
					this.isVip = ''
					this.moneyUser = ''
					this.isBirthday = ''
					this.integralNum = ''
					this.userVipDetails = ''
					this.dataSelect()
				}
				if (tab._props.label == '黑名单用户') {
					this.isAuthentication = ''
					this.statusT = 2
					this.isVip = ''
					this.isBirthday = ''
					this.moneyUser = ''
					this.integralNum = ''
					this.userVipDetails = ''
					this.dataSelect()
				}
			},
			// 状态
			change(val, userId) {
				this.$http({
					url: this.$http.adornUrl(`user/updateUserStatusByUserId?userId=${userId}`),
					method: 'get',
					data: this.$http.adornData({})
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
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					}
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
			// 详情跳转
			updates2(row) {
				// this.$router.push({
				// 	path: '/userDetail',
				// 	query: {
				// 		userId: row.superior
				// 	}
				// })
				this.$http({
					url: this.$http.adornUrl('user/selectUserByInvitationCode'),
					method: 'get',
					params: this.$http.adornParams({
						'invitationCode': row.inviterCode
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$router.push({
							path: '/userDetail',
							query: {
								userId: data.data.userEntity.userId
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {}
						})
					}
				})
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
				this.paySelect()
			},
			handleCurrentChange1(val) {
				this.page = val
				this.paySelect()
			},
			// select自动选择
			selectTrigger() {
				this.dataSelect()
			},
			// 查询
			select() {
				this.page = 1
				this.dataSelect()
			},
			select2() {
				this.paySelect()
			},
			// 重置
			cleans() {
				this.page = 1
				this.startTime = ''
				this.endTime = ''
				this.userName = ''
				this.inviterCode = ''
				this.phone = ''
				this.isRateT = ''
				this.vipIndexT = ''
				this.vipNo = ''
				this.birthdayMonth = ''
				this.dataSelect()
			},
			//删除用户
			deleteuser(row) {
				let delid = row.userId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`user/deleteUserByUserId/${delid}`),
						method: 'post',
						params: this.$http.adornData({})
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
								type: 'warning',
								duration: 1500,
								onClose: () => {}
							})
						}
					})
				})
			},
			// 设置陪玩官
			updates1(row) {
				this.$confirm(`确定进行此操作?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('user/updateUserIsAuthentication'),
						method: 'get',
						params: this.$http.adornParams({
							'userId': row.userId
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
						} else {
							this.$message({
								message: data.msg,
								type: 'warning',
								duration: 1500,
								onClose: () => {}
							})
						}
					})
				})
			},
			// 设置会员
			updates3(row) {
				this.$confirm(`确定要设置1个月会员?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('user/updateUserVip'),
						method: 'post',
						params: this.$http.adornParams({
							'userId': row.userId
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
						} else {
							this.$message({
								message: data.msg,
								type: 'warning',
								duration: 1500,
								onClose: () => {}
							})
						}
					})
				})
			},
			// 获取社区数据列表
			homeSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('helpCampus/selectCampusList'),
					method: 'get',
					params: this.$http.adornParams({
						// 'page':this.page,
						// 'limit':this.limit,
						// 'content':this.content
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.homeData1 = returnData
				})
			},
			// 筛选信息
			animeDat2(state) {
				this.dataSelect()
			},
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('user/selectUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userName': this.userName,
						'inviterCode': this.inviterCode,
						'startTime': this.startTime,
						'endTime': this.endTime,
						'isAuthentication': this.isAuthentication,
						'phone': this.phone,
						'status': this.statusT,
						'isRate': this.isRateT,
						'isVip': this.isVip,
						'moneyUser': this.moneyUser,
						'vipIndex': this.vipIndexT,
						'vipNo': this.vipNo,
						'isBirthday': this.isBirthday,
						'birthdayMonth':this.birthdayMonth,
						'integralNum':this.integralNum,
						'userVipDetails':this.userVipDetails,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					if (data.data.list.length > 0) {
						for (var i in data.data.list) {
							if (data.data.list[i].vipIndex) {
								for (var j in this.tableDataVip) {
									if (data.data.list[i].vipIndex == this.tableDataVip[j].vipIndex) {
										data.data.list[i].vipName = this.tableDataVip[j].vipName
									}
								}
							}
						}

					}

					let returnData = data.data
					this.tableData = returnData
				})
			},
			xiugai(row, text) {
				this.titleBl = '修改佣金比例'
				if (text == 'rate') {
					this.proportion = row.rate
				}
				if (text == 'zhiRate') {
					this.proportion = row.zhiRate
				}
				if (text == 'feiRate') {
					this.proportion = row.feiRate
				}
				if (text == 'consortiaOrdersRate') {
					this.titleBl = '公会订单抽成比例'
					this.proportion = row.consortiaOrdersRate
				}
				if (text == 'consortiaGiftRate') {
					this.titleBl = '公会礼物抽成比例'
					this.proportion = row.consortiaGiftRate
				}
				if (text == 'vipNo') {
					this.titleBl = '会员编号'
					this.proportion = row.vipNo
				}
				this.texts = text
				this.userIdss = row.userId
				this.dialogFormVisible2 = true
			},
			StairNoticeTo2() {
				var datas = {}
				if (this.texts == 'rate') {
					datas.rate = this.proportion
				}
				if (this.texts == 'zhiRate') {
					datas.zhiRate = this.proportion
				}
				if (this.texts == 'feiRate') {
					datas.feiRate = this.proportion
				}
				if (this.texts == 'consortiaOrdersRate') {
					datas.consortiaOrdersRate = this.proportion
				}
				if (this.texts == 'consortiaGiftRate') {
					datas.consortiaGiftRate = this.proportion
				}
				if (this.texts == 'vipNo') {
					datas.vipNo = this.proportion
				}
				if (this.proportion === '' || this.proportion == null) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入' + this.titleBl,
						type: 'warning'
					})
					return
				}
				datas.userId = this.userIdss
				this.$http({
					url: this.$http.adornUrl('user/updateUserByUserId'),
					method: 'post',
					data: this.$http.adornData(datas)
				}).then(({
					data
				}) => {
					console.log('data', data)
					this.$message({
						message: '修改成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.dialogFormVisible2 = false
							this.dataSelect()
						}
					})
				})
			},
			exportUser() {
				let search = this.platform == 'web' ? 'wangyexiazai' : this.platform
				let phone = -1
				if (this.phone) {
					phone = this.phone
				}
				this.$http({
					url: this.$http.adornUrl('admin/admin/admin_daochu_user'),
					method: 'get',
					responseType: 'blob',
					params: this.$http.adornParams({
						'userName': this.userName,
						'inviterCode': this.inviterCode,
						'startTime': this.startTime,
						'endTime': this.endTime,
						'isAuthentication': this.isAuthentication,
						'phone': this.phone,
						'status': this.statusT,
						'isRate': this.isRateT,
						'isVip': this.isVip,
						'moneyUser': this.moneyUser,
						'vipIndex': this.vipIndexT,
						'vipNo': this.vipNo,
						'isBirthday': this.isBirthday,
						'birthdayMonth':this.birthdayMonth,
						'integralNum':this.integralNum,
						'userVipDetails':this.userVipDetails,
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
						elink.download = '用户列表.xlsx'
						elink.style.display = 'none'
						elink.href = url
						document.body.appendChild(elink)
						elink.click()
						document.body.removeChild(elink)
					}
				})
			},
			// 是否是推广用户
			changeTg(val, userId) {
				this.$http({
					url: this.$http.adornUrl('user/updateUserByUserId'),
					method: 'post',
					data: this.$http.adornData({
						'userId': userId,
						'isRate': val
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
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					}
				})
			},
			// 查看二维码
			erweima(row) {
				this.dialogFormVisibleEwm = true
				let storeId = this.$route.query.storeId
				this.erweimaImg = this.$http.adornUrl('app/invite/mpCreateQr?invitationCode=' + row.invitationCode +
					'&page=')
			},
			updatesVip(row) {
				this.userIdss = row.userId
				if (row.vipIndex) {
					this.vipIndex = row.vipIndex
				} else {
					this.vipIndex = ''
				}

				this.dataSelectVip()
				this.Loading = false
				this.dialogFormVisible3 = true
			},
			dataSelectVip() {

				this.$http({
					url: this.$http.adornUrl('vip/selectVipList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 20,
						'vipName': '',
						'isEnable': '',
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.tableDataVip = returnData.records
					this.dataSelect()
					// var datas = {
					// 	vipName:'非会员用户',
					// 	vipIndex:0,
					// }
					// this.tableDataVip.push(datas)
				})
			},
			StairNoticeTo3() {
				this.Loading = true
				var datas = {}
				datas.vipIndex = this.vipIndex
				datas.userId = this.userIdss
				this.$http({
					url: this.$http.adornUrl('user/updateUserByUserId'),
					method: 'post',
					data: this.$http.adornData(datas)
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						console.log('data', data)
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dialogFormVisible3 = false
								this.Loading = false
								this.dataSelect()
							}
						})
					} else {

						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {}
						})
					}

				})
			},
			// 新增用户弹框
			addUserBtn() {
				this.vipIndex = ''
				this.phoneAdd = ''
				this.dialogFormVisible4 = true
				this.Loading = false
			},
			// 确认新增
			addNoticeTo() {
				this.Loading = true
				if (this.phoneAdd == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入手机号',
						type: 'warning'
					})
					return this.Loading = false
				}
				var datas = {}
				// datas.vipIndex = this.vipIndex
				datas.phone = this.phoneAdd
				this.$http({
					url: this.$http.adornUrl('user/insertUser'),
					method: 'post',
					data: this.$http.adornData(datas)
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						console.log('data', data)
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.Loading = false
								this.dialogFormVisible4 = false
								this.dataSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {}
						})
					}

				})
			},
			// 增送会员
			updateVip(row){
			
				this.$http({
					url: this.$http.adornUrl('vipDetails/giveUserVip'),
					method: 'post',
					params: this.$http.adornParams({
						'userId':row.userId,
						'day':30
					})
				}).then(({
					data
				}) => {
					if(data.code==0){
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					}else{
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					}
			
				})
			},
			// 取消会员
			quxiaoVip(row){
				this.$confirm(`确定要取消会员?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
				this.$http({
					url: this.$http.adornUrl('vipDetails/removeUserVip'),
					method: 'post',
					// data: this.$http.adornData({
					params: this.$http.adornParams({
						'userId':row.userId
					})
				}).then(({
					data
				}) => {
					if(data.code==0){
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					}else{
				this.$message({
					message: data.msg,
					type: 'warning',
					duration: 1500,
					onClose: () => {
					}
				})
					}
			
				})
				})
			},
		},
		mounted() {
			// this.dataSelectVip()
			this.dataSelect()
		}
	}
</script>

<style scoped>
.user-list-page__filter-input {
	width: 150px;
}

.user-list-page__filter-select {
	width: 150px;
}

.user-list-page__filter-date {
	width: 180px;
}

.user-list-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.user-list-page__tabs {
	margin-bottom: 0;
}

.user-list-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.user-list-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.user-list-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.user-list-page__tabs .nf-filter-card {
	margin-top: 12px;
}

.user-list-page__tabs .nf-table-card {
	margin-top: 12px;
	margin-bottom: 0;
}

.user-list-page__link {
	color: #5cc477;
	cursor: pointer;
}

.user-list-page__link:hover {
	color: #3fae5c;
}

.user-list-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.nf-table-card .el-button + .el-button {
	margin-left: 0 !important;
	margin-top: 5px !important;
}
</style>
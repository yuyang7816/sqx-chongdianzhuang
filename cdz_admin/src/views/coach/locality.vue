<template>
	<div class="nf-page locality-jl-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">教练管理</span>
			<h2 class="nf-page-hero__title">教练项目中心</h2>
			<p class="nf-page-hero__desc">管理教练服务项目，支持按城市、项目名称筛选，可进行添加、修改、上下架与加钟项目配置。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<el-tabs v-model="activeName" @tab-click="handleClick" class="locality-jl-page__tabs">
			<!-- 项目列表 -->
			<el-tab-pane label="项目列表" name="third">
				<div class="nf-filter-card">
					<div class="nf-filter-card__head">
						<i class="el-icon-search nf-filter-card__head-icon"></i>
						<span>筛选条件</span>
					</div>
					<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
						<el-form-item label="城市">
							<el-input class="locality-jl-page__filter-input" @keydown.enter.native="phoneSelect1"
								placeholder="请输入城市" v-model="citys">
							</el-input>
						</el-form-item>
						<el-form-item label="项目名称">
							<el-input class="locality-jl-page__filter-input" @keydown.enter.native="phoneSelect1"
								placeholder="请输入项目名称" v-model="title">
							</el-input>
						</el-form-item>
						<el-form-item class="locality-jl-page__filter-actions">
							<el-button type="primary" icon="el-icon-search" @click="phoneSelect1">查询</el-button>
							<el-button type="primary" icon="el-icon-refresh-left" @click="cleans3">重置</el-button>
							<el-button type="primary" icon="el-icon-plus" :disabled="!isAuth('locality:add')"
								@click="classAdd(0)">添加</el-button>
						</el-form-item>
					</el-form>
				</div>

				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData.list">
					<el-table-column prop="massageTypeId" label="编号" width="80" fixed="left">
					</el-table-column>
					<!-- <el-table-column prop="isShopOrHome" label="类型">
						<template slot-scope="scope">
							<span v-if="scope.row.isShopOrHome==1">上门</span>
							<span v-if="scope.row.isShopOrHome==2">到店</span>
						</template>
					</el-table-column> -->
					<!-- <el-table-column prop="classifyName" label="项目类型" fixed="left" width="150">
					</el-table-column> -->
					
					<el-table-column prop="title" label="项目名称" fixed="left" width="150">
					</el-table-column>
					<el-table-column prop="massageImg" label="项目图片">
						<template slot-scope="scope">
							　　<img v-if="scope.row.massageImg &&scope.row.massageImg!=''" :src="scope.row.massageImg"
								width="40" height="40" />
							<span v-else>暂无图片</span>
						</template>
					</el-table-column>
					<el-table-column prop="certificateImg" label="项目详情图" width="250">
						<template slot-scope="scope">
							<div v-if="scope.row.contentImg">
								<div v-for="(item,index) in scope.row.contentImg.split(',')" :key="index"
									style="display: inline-block; margin: 3px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
										<img style="width: 300px; height: auto" :src="item" alt="">
									</el-popover>
								</div>
							</div>
							
						</template>
					</el-table-column>
					<el-table-column prop="region" label="性别要求" width="100">
						<template slot-scope="scope">
							<span v-if="scope.row.isSex==0">不限制</span>
							<span v-if="scope.row.isSex==1">男</span>
							<span v-if="scope.row.isSex==2">女</span>
						</template>
					</el-table-column>
					<el-table-column prop="applyPeople" label="适用人群">
					</el-table-column>
					<el-table-column prop="region" label="城市" width="100">
						<template slot-scope="scope">
							<span v-if="scope.row.city">{{scope.row.city}}</span>
							<span v-else>不限</span>
						</template>
					</el-table-column>
					<el-table-column prop="oldPrice" label="原价">
					</el-table-column>
					<el-table-column prop="price" label="现价">
					</el-table-column>
					<el-table-column prop="memberPrice" label="会员价">
					</el-table-column>
					<!-- <el-table-column prop="artificerPrice" label="教练价">
					</el-table-column> -->
					<el-table-column prop="duration" label="时长(分钟)">
					</el-table-column>
					<el-table-column prop="addNum" label="加钟次数">
					</el-table-column>

					<el-table-column prop="labels" label="标签" width="200">
					</el-table-column>
					<el-table-column prop="sales" label="销量">
						<template slot-scope="scope">
							<span>{{scope.row.sales}}</span>
							<el-button size="mini" type="primary" plain  :disabled="!isAuth('locality:update')" 
								@click="updateBz(scope.row)" style="margin:5px;">修改</el-button>
						</template>
					</el-table-column>
					<el-table-column prop="content" label="详情" width="300">
					</el-table-column>

					<el-table-column prop="status" label="状态">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.status"
								@change="change(scope.row.massageTypeId,scope.row.status)"
								:disabled="!isAuth('locality:update')" :active-value="openValue2"
								:inactive-value="closeValue2" active-color="#5cc477" inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="170" fixed='right'>
						<template slot-scope="scope">
							<el-button size="mini" type="primary" class="locality-jl-page__op-btn" @click="jiazhongBtn( scope.row)">加钟项目
							</el-button>
							<el-button size="mini" type="primary" class="locality-jl-page__op-btn" :disabled="!isAuth('locality:update')"
								@click="classAdd(scope.row)">修改
							</el-button>
							<el-button size="mini" type="danger" class="locality-jl-page__op-btn" :disabled="!isAuth('locality:delete')"
								@click="classdelete(scope.row)">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="locality-jl-page__pagination">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<!-- 添加/修改教练弹框 -->
			<el-dialog :title="renwuTitles" customClass="customWidth" :visible.sync="dialogFormVisible5" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">项目：</span>
					<el-checkbox-group v-model="checkList" @change="onChang" style="display: inline;">
						<el-checkbox v-for="(item,index) in homeData" :key="index" :label="item.gameName">
							{{item.gameName}}
						</el-checkbox>
					</el-checkbox-group>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">发布人：</span>
					<el-input v-model="nickName1" @focus="userselect" style="width:45%;" placeholder="请选择发布人">
					</el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">标题：</span>
					<el-input v-model="myLevel" style="width:45%;" placeholder="请输入标题">
					</el-input>
				</div>
				<div style="margin-bottom: 10px;display:flex;">
					<span style="width: 200px;display: inline-block;text-align: right;">主页图：</span>
					<div
						style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
						<el-upload class="avatar-uploader" v-model="headImg"
							:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
							:on-success="handleAvatarSuccess5" :on-progress="onprogress">
							<img v-if="headImg!=''&&percentage==100" :src="headImg" class="avatar"
								style="border-radius: 6px;width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon iconss"></i>
							<el-progress v-if="percentage>0&&percentage<100" type="circle" :percentage="percentage">
							</el-progress>
						</el-upload>
					</div>
				</div>
				<div style="margin-bottom: 10px;display:flex;" v-if="authentication==1">
					<span style="width: 200px;display: inline-block;text-align: right;">资格证书：</span>
					<div class="imgs" v-for="(item,index) in certificateImg" :key="index">
						<img width="100%" class="images" height="100%" :src="item" alt="">
						<span class="dels">
							<i class="el-icon-delete" @click="clear1(index)"></i>
						</span>
					</div>
					<div class="imgs" style="width: 50%;">
						<el-upload :action="$http.adornUrlUp('alioss/uploadWatermark')"
							list-type="picture-card" :show-file-list="false" :on-success="handleUploadSuccess1"
							:on-progress="onprogress1">
							<el-progress v-if="percentage1>0 && percentage1<100" type="circle"
								:percentage="percentage1"></el-progress>
							<i v-else class="el-icon-plus"></i>
						</el-upload>
					</div>
				</div>
				<div style="margin-bottom: 10px;display:flex;" v-if="authentication==1">
					<span style="width: 200px;display: inline-block;text-align: right;">工作照片：</span>
					<div class="imgs" v-for="(item,index) in workImg" :key="index">
						<img width="100%" class="images" height="100%" :src="item" alt="">
						<span class="dels">
							<i class="el-icon-delete" @click="clear2(index)"></i>
						</span>
					</div>
					<div class="imgs" style="width: 50%;">
						<el-upload :action="$http.adornUrlUp('alioss/upload')"
							list-type="picture-card" :show-file-list="false" :on-success="handleUploadSuccess2"
							:on-progress="onprogress1">
							<el-progress v-if="percentage1>0 && percentage1<100" type="circle"
								:percentage="percentage1"></el-progress>
							<i v-else class="el-icon-plus"></i>
						</el-upload>
					</div>
				</div>
				<div style="margin-bottom: 10px;display:flex;" v-if="authentication==2">
					<span style="width: 200px;display: inline-block;text-align: right;">项目详情图：</span>
					<div class="imgs" v-for="(item,index) in contentImg" :key="index">
						<img width="100%" class="images" height="100%" :src="item" alt="">
						<span class="dels">
							<i class="el-icon-delete" @click="clear3(index)"></i>
						</span>
					</div>
					<div class="imgs" style="width: 50%;">
						<el-upload :action="$http.adornUrlUp('alioss/upload')"
							list-type="picture-card" :show-file-list="false" :on-success="handleUploadSuccess3"
							:on-progress="onprogress1">
							<el-progress v-if="percentage1>0 && percentage1<100" type="circle"
								:percentage="percentage1"></el-progress>
							<i v-else class="el-icon-plus"></i>
						</el-upload>
					</div>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">期望薪资：</span>
					<el-input v-model="money" type="number" style="width:45%;" min="0" placeholder="请输入期望薪资"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">工作经验：</span>
					<el-input v-model="orderLevel" type="text" style="width:45%;" placeholder="请输入工作经验"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">技能：</span>
					<el-input v-model="memberMoney" style="width:45%;" placeholder="请输入技能"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span
						style="width: 200px;display: inline-block;text-align: right;position: relative;bottom: 26px;">描述：</span>
					<el-input v-model="describes" type="textarea" :rows="2" style="width:45%;" placeholder="请输入特长描述">
					</el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">服务地区：</span>
					<el-cascader style="width:45%" size="large" :options="options" ref="cascaderAddr"
						v-model="storeAddress" :placeholder="storeAddre" @change="handleChange55" :value="storeAddre">
					</el-cascader>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">纬度：</span>
					<el-input v-model="latitude" style="width:45%;" placeholder="请输入纬度"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">经度：</span>
					<el-input v-model="longitude" style="width:45%;" placeholder="请输入经度"></el-input>
				</div>
				<div>
					<div id="container1" style="width:80%;height:500px;margin-left: 10%;"></div>
				</div>
				<div slot="footer" class="dialog-footer" style="margin-top: 30px;text-align: center;">
					<el-button @click="prev">取 消</el-button>
					<el-button type="primary" @click="addmissionNoticeTo()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 用户列表 -->
			<el-dialog title="用户列表" :visible.sync="dialogFormVisible3" center>
				<div style="margin:2% 0;display: inline-block;">
					<el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入手机号"
						v-model="phone1"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
					<el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入昵称"
						v-model="nickName2"></el-input>
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="userclick">
						查询
					</el-button>
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="userclose">
						重置
					</el-button>
				</div>
				<el-table width="780px" v-loading="tableDataLoading4" :data="userIds.records">
					<el-table-column fixed prop="nickName" width="220" label="昵称">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{scope.row.userName ? scope.row.userName : '未设置'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="phone" width="220" label="手机号">
						<template slot-scope="scope">
							<span>{{scope.row.phone ? scope.row.phone : '未绑定'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" width="220" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" fixed='right' width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="confirm(scope.row)">确定
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
						:page-sizes="[5, 10, 15, 20]" :page-size="size1" :current-page="page1"
						layout="total,sizes, prev, pager, next,jumper" :total="userIds.total">
					</el-pagination>
				</div>
			</el-dialog>
			<!--    图片展示-->
			<el-dialog title="图片" :visible.sync="dialogVisible" width="800px">
				<span v-if="imageUrl.length>0" v-for="(item, index) in imageUrl" :key="index" style="">
					<!-- <el-image style = "width: 100px; height: 100px" :src = "item" :preview-src-list="imageUrl"></el-image>-->
					<img :src="item" alt="" style="width: 45%; height: 100%;margin: 15px;display:inline-block;">
				</span>
				<span v-else>暂无图片</span>
			</el-dialog>
			<!-- 项目列表弹框 -->
			<el-dialog title="项目列表" :visible.sync="dialogFormVisible8" center width="80%">
				<div>
					<el-button style="margin: 10px 0;" :disabled="!isAuth('locality:add')" size="mini" type="primary"
						icon="document" @click="choiadd()">添加项目</el-button>
				</div>
				<el-table v-loading="tableDataLoading1" :data="tableData3">
					<el-table-column prop="massageTypeId" label="编号" width="80" fixed="left">
					</el-table-column>
					<el-table-column prop="title" label="项目名称" width="100" fixed="left">
					</el-table-column>
					<el-table-column prop="massageImg" label="项目图片">
						<template slot-scope="scope">
							　　<img v-if="scope.row.massageImg &&scope.row.massageImg!=''" :src="scope.row.massageImg"
								width="40" height="40" />
							<span v-else>暂无图片</span>
						</template>
					</el-table-column>
					<el-table-column prop="certificateImg" label="项目详情图" width="250">
						<template slot-scope="scope">
							<div v-if="scope.row.contentImg.length>0">
								<div v-for="(item,index) in scope.row.contentImg" :key="index"
									style="display: inline-block; margin: 3px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
										<img style="width: 300px; height: auto" :src="item" alt="">
									</el-popover>
								</div>
							</div>
							<div v-else>暂无图片</div>
						</template>
					</el-table-column>
					<el-table-column prop="applyPeople" label="适用人群">
					</el-table-column>
					<el-table-column prop="region" label="性别要求" width="100">
						<template slot-scope="scope">
							<span v-if="scope.row.isSex==0">不限制</span>
							<span v-if="scope.row.isSex==1">男</span>
							<span v-if="scope.row.isSex==2">女</span>
						</template>
					</el-table-column>
					<el-table-column prop="region" label="城市" width="100">
						<template slot-scope="scope">
							<span v-if="scope.row.city">{{scope.row.city}}</span>
							<span v-else>不限</span>
						</template>
					</el-table-column>
					<el-table-column prop="oldPrice" label="原价">
					</el-table-column>
					<el-table-column prop="price" label="现价">
					</el-table-column>
					<el-table-column prop="memberPrice" label="会员价">
					</el-table-column>
					<!-- <el-table-column prop="artificerPrice" label="教练价">
					</el-table-column> -->
					<el-table-column prop="duration" label="时长(分钟)">
					</el-table-column>
					<el-table-column prop="labels" label="标签" width="200">
					</el-table-column>
					<el-table-column prop="sales" label="销量">
					</el-table-column>
					<el-table-column prop="content" label="详情" width="300">
					</el-table-column>
					<el-table-column prop="artificerMassageStatus" label="状态">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.artificerMassageStatus"
								@change="change(scope.row.massageTypeId,scope.row.artificerMassageStatus)"
								:disabled="!isAuth('locality:update')" :active-value="openValue1"
								:inactive-value="closeValue1" active-color="#13ce66" inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="100" fixed='right' align="center">
						<template slot-scope="scope">
							<el-button size="mini" type="danger" :disabled="!isAuth('locality:delete')"
								@click="classListdelete(scope.row)">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-dialog>
			<!-- 添加、修改项目 -->
			<el-dialog :title="titles" :visible.sync="dialogFormVisible9" center width="80%">
				<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">类型：</span>
					<el-radio-group v-model="ordersType">
						<el-radio :label="1">上门</el-radio>
						<el-radio :label="2">到店</el-radio>
					</el-radio-group>
				</div> -->
				<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">项目类型：</span>
					<el-select v-model="classType" style="width:50%;">
						<el-option v-for="item in fwData" :key="item.id" :label="item.value" :value="item.id">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;
				</div> -->
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">项目名称：</span>
					<el-input style="width:50%;" v-model="massageTypeTitle" type="text" placeholder="请输入项目名称">
					</el-input>
				</div>
				<div style="margin-bottom: 10px;display:flex;">
					<span style="width: 200px;display: inline-block;text-align: right;">项目图片：</span>
					<div
						style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
						<el-upload class="avatar-uploader" v-model="massageImg"
							:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
							:on-success="handleAvatarSuccess1" :on-progress="onprogress2">
							<!-- <el-progress type="circle" :percentage="100" status="success"></el-progress> -->
							<img v-if="massageImg!=''" :src="massageImg" class="avatar"
								style="border-radius: 6px;width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon iconss"></i>
							<!-- <el-progress v-if="percentage2>0&&percentage2<100" type="circle" :percentage="percentage2">
							</el-progress> -->
						</el-upload>
					</div>
				</div>
				<div style="margin-bottom: 10px;display: flex;align-items: center;">
					<span style="width: 200px;display: inline-block;text-align: right;">项目详情图：</span>
					<div style="display: flex;flex-wrap: wrap;">
						<div class="imgs" v-for="(item,index) in contentImg" :key="index">
							<img width="100%" class="images" height="100%" :src="item" alt="">
							<span class="dels">
								<i class="el-icon-delete" @click="clear3(index)"></i>
							</span>
						</div>
						<div class="imgs">
							<el-upload :action="$http.adornUrlUp('alioss/upload')"
								list-type="picture-card" :show-file-list="false" :on-success="handleUploadSuccess3"
								:on-progress="onprogress1">
								<el-progress v-if="percentage1>0 && percentage1<100" type="circle"
									:percentage="percentage1"></el-progress>
								<i v-else class="el-icon-plus"></i>
							</el-upload>
						</div>
					</div>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">原价：</span>
					<el-input style="width:50%;" v-model="oldPrice"  type="number" min="0"
						placeholder="请输入原价"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">现价：</span>
					<el-input style="width:50%;" v-model="price" type="number" min="0"
						placeholder="请输入现价"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">会员价：</span>
					<el-input style="width:50%;" v-model="memberPrice"  type="number" min="0"
						placeholder="请输入会员价"></el-input>
				</div>
				<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">教练价：</span>
					<el-input style="width:50%;" v-model="artificerPrice"
						onkeyup="value=value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3')" type="number" min="0"
						placeholder="请输入教练价"></el-input>
				</div> -->
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">项目时长(分钟)：</span>
					<el-input style="width:50%;" v-model="duration" type="text" placeholder="请输入项目时长(分钟)"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">加钟次数：</span>
					<el-input style="width:50%;" v-model="addNum" type="text" placeholder="请输入加钟次数"></el-input>
				</div>

				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">性别限制：</span>
					<el-radio-group v-model="isSex">
						<el-radio :label="0">不限制</el-radio>
						<el-radio :label="1">男</el-radio>
						<el-radio :label="2">女</el-radio>
					</el-radio-group>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">适用人群：</span>
					<el-input style="width:50%;" v-model="applyPeople" type="text" placeholder="请输入适用人群"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">状态：</span>
					<el-radio-group v-model="massageTypeStatus">
						<el-radio :label="1">上线</el-radio>
						<el-radio :label="2">下线</el-radio>
					</el-radio-group>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">服务城市：</span>
					<el-cascader style="width:45%" size="large" :options="options" ref="cascaderAddr"
						v-model="storeAddress" :placeholder="storeAddre" @change="handleChange55" :value="storeAddre">
					</el-cascader>
					<el-button type="info" class="tj" size="mini" icon="el-icon-delete" circle @click="btnCs">
					</el-button>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">项目标签：</span>
					<span class="bqList" v-for="(item,index) in labels1" :key="index">{{item}}
						<span class="delss">
							<i class="el-icon-delete" @click="dels1(index)"></i>
						</span>
					</span>
					<el-input v-model="bq" style="width:200px;" placeholder="请输入标签"></el-input>
					<el-button type="primary" class="tj" size="mini" icon="el-icon-edit" circle @click="btnTj">
					</el-button>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">项目内容：</span>
					<!-- <quill-editor ref="myTextEditor" v-model="content" :options="quillOption"
						style="padding-bottom: 50px;height: 300px;width: 72%;display: inline-table;margin-bottom: 60px;">
					</quill-editor> -->
					<el-input v-model="content" type="textarea" :rows="5" style="width:50%;" placeholder="请输入项目内容">
					</el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible9 = false">取 消</el-button>
					<el-button type="primary" @click="refuseto1()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 评价列表 -->
			<el-dialog title="评价列表" :visible.sync="dialogFormVisible10" center width="80%">
				<el-table v-loading="tableDataLoading" :data="pinglunData.list">
					<el-table-column prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="userName" label="评价用户" width="120">
						<template slot-scope="scope">
							　<span style="color: #5cc477;cursor: pointer;" @click="updatesvideo(scope.row)"
								v-text="scope.row.userName"></span>
						</template>
					</el-table-column>
					<el-table-column prop="avatar" label="用户头像" width="100">
						<template slot-scope="scope">
							　　<img v-if="scope.row.avatar && scope.row.avatar!=''" :src="scope.row.avatar" width="40"
								height="40" />
							<span v-else>暂无图片</span>
						</template>
					</el-table-column>
					<el-table-column prop="content" label="评价内容">
					</el-table-column>
					<el-table-column prop="score" label="评分">
					</el-table-column>
					<el-table-column prop="createTime" label="评价时间" width="180">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="100" align="center">
						<template slot-scope="scope">
							<el-button size="mini" type="danger" :disabled="!isAuth('locality:delete')"
								@click="pinglundelete(scope.row)">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
						:page-sizes="[10, 20, 30, 40]" :page-size="size2" :current-page="page2"
						layout="total,sizes, prev, pager, next,jumper" :total="pinglunData.totalCount">
					</el-pagination>
				</div>
			</el-dialog>
			<el-dialog title="项目列表" :visible.sync="dialogFormVisible11" center>
				<el-table v-loading="tableDataLoading" :data="tableData4.list">
					<el-table-column prop="massageTypeId" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="title" label="项目名称">
					</el-table-column>
					<el-table-column prop="massageImg" label="项目图片">
						<template slot-scope="scope">
							　　<img v-if="scope.row.massageImg &&scope.row.massageImg!=''" :src="scope.row.massageImg"
								width="40" height="40" />
							<span v-else>暂无图片</span>
						</template>
					</el-table-column>
					<el-table-column prop="certificateImg" label="项目详情图" width="250">
						<template slot-scope="scope">
							<div v-for="(item,index) in scope.row.contentImg" :key="index"
								style="display: inline-block; margin: 3px;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
									<img style="width: 300px; height: auto" :src="item" alt="">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="region" label="性别要求" width="100">
						<template slot-scope="scope">
							<span v-if="scope.row.isSex==0">不限制</span>
							<span v-if="scope.row.isSex==1">男</span>
							<span v-if="scope.row.isSex==2">女</span>
						</template>
					</el-table-column>
					<el-table-column prop="applyPeople" label="适用人群">
					</el-table-column>
					<el-table-column prop="region" label="城市" width="100">
						<template slot-scope="scope">
							<span v-if="scope.row.city">{{scope.row.city}}</span>
							<span v-else>不限</span>
						</template>
					</el-table-column>
					<el-table-column prop="oldPrice" label="原价">
					</el-table-column>
					<el-table-column prop="price" label="现价">
					</el-table-column>
					<el-table-column prop="memberPrice" label="会员价">
					</el-table-column>
					<!-- <el-table-column prop="artificerPrice" label="教练价">
					</el-table-column> -->
					<el-table-column prop="duration" label="时长(分钟)">
					</el-table-column>
					<el-table-column prop="addNum" label="加钟次数">
					</el-table-column>

					<el-table-column prop="labels" label="标签" width="200">
					</el-table-column>
					<el-table-column prop="sales" label="销量">
					</el-table-column>
					<el-table-column prop="content" label="详情" width="300">
					</el-table-column>

					<el-table-column prop="status" label="状态">
						<template slot-scope="scope">
							<span v-if="scope.row.status==1">上线</span>
							<span v-if="scope.row.status==2">下线</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="80" fixed='right' align="center">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('locality:add')"
								@click="classListAdd(scope.row)">添加
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange4" @current-change="handleCurrentChange4"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit3" :current-page="page3"
						layout="total,sizes, prev, pager, next,jumper" :total="tableData4.totalCount">
					</el-pagination>
				</div>
			</el-dialog>
			<!-- 加钟项目 -->
			<el-dialog title="加钟项目" :visible.sync="dialogFormVisibleJz" center width="80%">
				<div style="text-align: right;display: inline-block;">
					<el-button style="margin:10px;" size="mini" type="primary" icon="document"
						:disabled="!isAuth('locality:add')" @click="classAddJz(0)">添加
					</el-button>
				</div>
				<el-table v-loading="tableDataLoadingJz" :data="tableDataJz.list">
					<el-table-column prop="massageTypeId" label="编号" width="80" fixed="left">
					</el-table-column>
					<el-table-column prop="title" label="项目名称" fixed="left" width="150">
					</el-table-column>
					<el-table-column prop="massageImg" label="图片">
						<template slot-scope="scope">
							　　<img v-if="scope.row.massageImg &&scope.row.massageImg!=''" :src="scope.row.massageImg"
								width="40" height="40" />
							<span v-else>暂无图片</span>
						</template>
					</el-table-column>
					<el-table-column prop="price" label="现价">
					</el-table-column>
					<el-table-column prop="memberPrice" label="会员价">
					</el-table-column>
					<!-- <el-table-column prop="artificerPrice" label="教练价">
					</el-table-column> -->
					<el-table-column prop="duration" label="时长(分钟)">
					</el-table-column>
					<el-table-column prop="status" label="状态">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.status"
								@change="change(scope.row.massageTypeId,scope.row.status)"
								:disabled="!isAuth('locality:update')" :active-value="openValue2"
								:inactive-value="closeValue2" active-color="#13ce66" inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="170" fixed='right'>
						<template slot-scope="scope">
							<el-button size="mini" type="primary" style="margin: 5px;" :disabled="!isAuth('locality:update')"
								@click="classAddJz(scope.row)">修改
							</el-button>
							<el-button size="mini" type="danger" style="margin: 5px;" :disabled="!isAuth('locality:delete')"
								@click="classdeleteJz(scope.row)">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeJz" @current-change="handleCurrentChangeJz"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tableDataJz.totalCount">
					</el-pagination>
				</div>
			</el-dialog>
			<!-- 添加、修改加钟项目 -->
			<el-dialog :title="titles" :visible.sync="dialogFormVisibleJzA" center>
				<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">项目类型：</span>
					<el-select v-model="classType" style="width:50%;">
						<el-option v-for="item in fwData" :key="item.id" :label="item.value" :value="item.id">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;
				</div> -->
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">项目名称：</span>
					<el-input style="width:50%;" v-model="massageTypeTitle" type="text" placeholder="请输入项目名称">
					</el-input>
				</div>
				<div style="margin-bottom: 10px;display:flex;">
					<span style="width: 200px;display: inline-block;text-align: right;">项目图片：</span>
					<div
						style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
						<el-upload class="avatar-uploader" v-model="massageImg"
							:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
							:on-success="handleAvatarSuccess1" :on-progress="onprogress2">
							<!-- <el-progress type="circle" :percentage="100" status="success"></el-progress> -->
							<img v-if="massageImg!=''" :src="massageImg" class="avatar"
								style="border-radius: 6px;width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon iconss"></i>
							<!-- <el-progress v-if="percentage2>0&&percentage2<100" type="circle" :percentage="percentage2">
							</el-progress> -->
						</el-upload>
					</div>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">现价：</span>
					<el-input style="width:50%;" v-model="price"  type="number" min="0"
						placeholder="请输入现价"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">会员价：</span>
					<el-input style="width:50%;" v-model="memberPrice"  type="number" min="0"
						placeholder="请输入会员价"></el-input>
				</div>
				<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">教练价：</span>
					<el-input style="width:50%;" v-model="artificerPrice"
						onkeyup="value=value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3')" type="number" min="0"
						placeholder="请输入教练价"></el-input>
				</div> -->
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">项目时长(分钟)：</span>
					<el-input style="width:50%;" v-model="duration" type="text" placeholder="请输入项目时长(分钟)"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">状态：</span>
					<el-radio-group v-model="massageTypeStatus">
						<el-radio :label="1">上线</el-radio>
						<el-radio :label="2">下线</el-radio>
					</el-radio-group>
				</div>

				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleJzA = false">取 消</el-button>
					<el-button type="primary" @click="refusetoJz()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 信用分明细 -->
			<el-dialog title="信用分明细" :visible.sync="dialogFormVisibleXyf" center>
				<el-table v-loading="tableDataLoadingXyf" :data="tableDataXyf.list">
					<el-table-column prop="id" label="编号" width="80">
					</el-table-column>
					<!-- <el-table-column prop="userName" label="投诉的用户" width="100">
						<template slot-scope="scope">
							<span style="color: #5cc477;cursor: pointer;" @click="updatesvideo(scope.row)">
								{{ scope.row.userName?scope.row.userName:'未绑定'}}
							</span>
						</template>
					</el-table-column> -->
					<el-table-column prop="title" label="标题" width="150">
					</el-table-column>
					<el-table-column prop="content" label="内容">
					</el-table-column>
					<!-- <el-table-column prop="image" label="图片" width="150">
						<template slot-scope="scope">
							<div v-if="scope.row.image">
								<div v-for="(item,index) in scope.row.image.split(',')" :key="index"
									style="display: inline-block; margin: 3px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
										<img style="width: 300px; height: auto" :src="item" alt="">
									</el-popover>
								</div>
							</div>
						</template>
					</el-table-column> -->
					<el-table-column prop="createAt" label="创建时间" width="100"></el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeXyf" @current-change="handleCurrentChangeXyf"
						:page-sizes="[10, 20, 30, 40]" :page-size="size1" :current-page="page1"
						layout="total,sizes, prev, pager, next,jumper" :total="tableDataXyf.totalCount">
					</el-pagination>
				</div>
			</el-dialog>
			<!-- 修改销量 -->
			<el-dialog title="修改销量" :visible.sync="dialogFormVisibleBz" center>
				<div style="margin-bottom: 10px;">
					<span
						style="width: 200px;display: inline-block;text-align: right;">销量：</span>
					<el-input style="width:50%;" v-model="content" type="number" min="0" placeholder="请输入销量">
					</el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleBz = false">取 消</el-button>
					<el-button type="primary" @click="replyNoticeTo()">确 定</el-button>
				</div>
			</el-dialog>
		</el-tabs>
	</div>
</template>

<style scoped>
.locality-jl-page__tabs {
	margin-bottom: 0;
}

.locality-jl-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.locality-jl-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.locality-jl-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.locality-jl-page__tabs .nf-filter-card {
	margin-top: 12px;
}

.locality-jl-page__tabs .nf-table-card {
	margin-top: 12px;
	margin-bottom: 0;
}

.locality-jl-page__filter-input {
	width: 200px;
}

.locality-jl-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.locality-jl-page__op-btn {
	margin: 5px 0;
}

.locality-jl-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.nf-table-card .el-button + .el-button {
	margin-left: 0 !important;
	margin-top: 5px !important;
}
</style>

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
				dialogWidth: 0,
				size: 10,
				page: 1,
				size1: 10,
				page1: 1,
				size2: 10,
				page2: 1,
				page3: 1,
				limit3: 10,
				state: '',
				limit: 10,
				classify: 6,
				classifys: 6,
				openValue: 1,
				closeValue: 2,
				openValue1: 0,
				closeValue1: 1,
				openValue2: 1,
				closeValue2: 2,
				title: '',
				type: '',
				name: '',
				keyword: '',
				describes: '',
				checkBoxData: [], //多选框选择的值
				method: 'false',
				formLabelWidth: '200px',
				activeName: 'third',
				tableDataLoading: true,
				tableDataLoading1: false,
				tableDataLoading4: false,
				dialogFormVisible3: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible5: false,
				dialogFormVisible6: false,
				dialogFormVisible7: false,
				dialogFormVisible8: false,
				dialogFormVisible9: false,
				dialogFormVisible10: false,
				dialogFormVisible11: false,
				homeData: [],
				homeData1: [{
					massageTypeId: 0,
					title: '全部'
				}],
				classify: 2,
				classifyList: [
					// {
					// 	id: '',
					// 	name: '全部'
					// },
					// {
					// 	id: 1,
					// 	name: '线上'
					// },
					{
						id: 2,
						name: '线下'
					}
				],
				choicenData: [],
				pinglunData: {},
				renwuId: '',
				url: '',
				imageUrl: '',
				id: '',
				userId: '',
				artificerName: '',
				myPhone: '',
				massageTypeId: '',
				statusId: '',
				statuss: [{
						id: '',
						title: '全部'
					},
					{
						id: 0,
						title: '服务中'
					},
					{
						id: 1,
						title: '待审核'
					},
					{
						id: 2,
						title: '已下架'
					},
					{
						id: 3,
						title: '拒绝'
					}
				],
				// 发布信息
				myLevel: '', //标题
				orderLevel: '', //接单段位
				userIdss: '', //用户id
				orderTakingArea: '', //接单大区
				homepageImg: '', //封面图
				headImg: '', //主页图
				percentage: 0, //进度条
				percentage2: 0, //进度条
				voiceIntroduce: '', //语音介绍
				gameId: '', //项目id
				oldMoney: '', //发布价格
				money: '', //普通用户价格
				orderLevel: '', //经验
				memberMoney: '', //会员价格
				latitude: '', //纬度
				longitude: '', //经度
				city: '', //市
				orderTakingTime: '',
				status: '', //状态0待审核  1上架  2下架 3驳回 (修改添加不传)
				region: '', //最低项目时间
				unit: '小时',
				phone: '', //电话
				phone1: '',
				pickeroptions: {
					selectableRange: '00:00:01 - 23:59:59'
				},
				value1: '',
				value2: '',

				options: provinceAndCityData,
				storeAddress: [],
				storeAddre: '请选择城市',
				province: '', //省
				district: '', //区
				ids: '', //信息id
				nickName: '',
				nickName1: '',
				nickName2: '',
				userIds: {}, //用户列表
				bq: '', //输入的标签名
				missions: 0,
				shenheId: 0, //审核id
				contents: '', //驳回理由
				dialogVisible: false,
				radio: 0,
				multipleSelection: [],
				campus: '',
				campus1: '',
				campusName: '',
				homeData2: [],
				quillOption: quillConfig,
				statusIdd: 1,
				tableData: {},
				tableData3: [],
				tableData4: {},
				titles: '添加项目项目',
				renwuTitles: '添加任务',
				gameName: '',
				gameIds: '',
				gameImg: '',
				gameStatus: '',
				checkList: [], //多选 选中集合
				detailsImg: [], //轮播图
				certificateImg: [], //资格证书
				workImg: [], //工作照片
				hideUpload: false,
				percentage1: 0,
				authentication: 1,

				// 教练分类参数
				labels1: [], //标签集合
				bq: '', //输入的标签名
				artificerPrice: '', //教练价格
				city: '', //教练城市
				content: '', //内容
				contentImg: [], //详情图
				duration: '', //时长
				addNum:'',//加钟次数
				isSex: '', //教练性别
				applyPeople:'',//适用人群
				labels: '', //标签
				massageImg: '', //图片
				massageTypeId: '', //教练分类Id
				memberPrice: '', //会员价
				oldPrice: '', //原价
				price: '', //现价
				sales: '', //销量
				massageTypeStatus: '', //教练分类状态
				massageTypeTitle: '', //标题
				citys: '',
				artificerId: '',
				userId1: '',

				classTypeList: [],
				// 加钟项目
				dialogFormVisibleJz:false,
				fwData:[],
				classifyId:'',
				classType: '',
				parentId:'',
				tableDataJz:{},
				tableDataLoadingJz:false,
				dialogFormVisibleJzA:false,
				pageJ: 1,
				limitJ: 10,
				dialogFormVisibleXyf:false,
				tableDataLoadingXyf:false,
				tableDataXyf:{},
				
				dialogFormVisibleBz:false,
				ordersId:'',
				content: '',
				serveTime:'',
				ordersType:'',
				ordersTypeT:'',
				typeNum: [
					{
						value: 1,
						label: '上门'
					},
					{
						value: 2,
						label: '到店'
					},
				],
			}
		},
		methods: {
			//查看照片
			refund(e) {
				console.log(e)
				this.imageUrl = []
				if (e.img != null) {
					let img = e.img.split(',')
					if (img.length != 0) {
						this.imageUrl = img
					}
				}
				this.dialogVisible = true
			},
			// 多选
			changeFun(val) {
				this.checkBoxData = val
			},
			handleSizeChange(val) {
				this.limit = val
				this.classSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.classSelect()
			},
			handleSizeChange1(val) {
				this.limit = val
				this.InformationSelect()
			},
			handleCurrentChange1(val) {
				this.page = val
				this.InformationSelect()
			},
			handleSizeChange2(val) {
				this.size1 = val
				this.tableDataLoading4 = true
				this.userClass()
			},
			handleCurrentChange2(val) {
				this.page1 = val
				this.tableDataLoading4 = true
				this.userClass()
			},
			handleSizeChange3(val) {
				this.size2 = val
				this.pinglunSelect(this.renwuId)
			},
			handleCurrentChange3(val) {
				this.page2 = val
				this.pinglunSelect(this.renwuId)
			},
			handleSizeChange4(val) {
				this.limit3 = val
				this.classSelect2(this.artificerId)
			},
			handleCurrentChange4(val) {
				this.page3 = val
				this.classSelect2(this.artificerId)
			},
			handleSizeChangeJz(val) {
				this.limitJ = val
				this.classSelectJz()
			},
			handleCurrentChangeJz(val) {
				this.pageJ = val
				this.classSelectJz()
			},
			handleSizeChangeXyf(val) {
				this.size1 = val
				this.xinyongSelect(this.artificerId)
			},
			handleCurrentChangeXyf(val) {
				this.page1 = val
				this.xinyongSelect(this.artificerId)
			},
			handleClick(tab, event) {
				this.campus = ''
				this.campus1 = ''
				this.citys = ''
				this.page = 1
				if (tab._props.label == '教练列表') {
					this.authentication = 1
					this.type = 2
					this.InformationSelect()
				}

				if (tab._props.label == '项目列表') {

					this.classSelect()
				}
			},

			
			// 查询
			phoneSelect() {
				this.page = 1
				this.InformationSelect()
			},
			// 重置
			cleans2() {
				this.status = ''
				this.statusId = ''
				this.artificerName = ''
				this.massageTypeId = ''
				this.classifyIds = ''
				this.citys = ''
				this.classifyId = ''
				
				this.page = 1
				console.log('this.phone', this.myPhone)
				this.InformationSelect()
			},
			// 查询
			phoneSelect1() {
				this.page = 1
				this.classSelect()
			},
			// 重置
			cleans3() {
				this.citys = ''
				this.title = ''
				this.classifyId = ''
				this.ordersTypeT = ''
				this.page = 1
				this.classSelect()
			},
			// 获取项目项目启用列表
			homeSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl(`artificer/selectMassageTypeList`),
					method: 'get',
					params: this.$http.adornParams({
						'status': 0
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.homeData1 = [{
						massageTypeId: '',
						title: '全部'
					}]
					if (data.data.length > 0) {
						for (var i in data.data) {
							this.homeData1.push(data.data[i])
							data.data[i].state = Number(data.data[i].state)
						}
					}
					this.homeData = data.data
				})
			},
			// 信息数据
			InformationSelect() {
				this.userId = this.$store.state.user.id
				var massageTypeId = ''
				if (this.massageTypeId != '') {
					massageTypeId = this.massageTypeId
				}
				this.$http({
					url: this.$http.adornUrl(`artificer/selectArtificerList`),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'artificerName': this.artificerName,
						'massageTypeId': this.massageTypeId,
						'longitude': '',
						'latitude': '',
						'city': this.citys,
						'classifyId':this.classifyId,
						
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.choicenData = returnData
					for (var i in this.choicenData.list) {
						if (this.choicenData.list[i].lifePhoto) {
							this.choicenData.list[i].lifePhoto = this.choicenData.list[i].lifePhoto
								.split(',')
						}
						if (this.choicenData.list[i].certificate) {
							this.choicenData.list[i].certificate = this.choicenData.list[i].certificate.split(',')
						}
					}

				})
			},
			// 封面图片上传
			handleAvatarSuccess5(file) {
				this.headImg = file.data;
			},
			onprogress(event, file, fileList) {
				console.log('event, file, fileList', parseInt(event.percent))
				this.percentage = parseInt(event.percent)
			},
			// 封面图片上传
			handleAvatarSuccess1(file) {
				this.massageImg = file.data;
			},
			onprogress2(event, file, fileList) {
				console.log('event, file, fileList', parseInt(event.percent))
				this.percentage2 = parseInt(event.percent)
			},
			
			// 删除详情图
			dels(index) {
				this.img.splice(index, 1);
				console.log(this.img)
			},
			// 获取分类id
			onChang(e) {
				console.log(e)
				this.gameId = e.toString()
				this.checkList = e
			},
			
			// 获取省市区
			handleChange55(value) {
				value = this.$refs['cascaderAddr'].currentLabels
				console.log('````````````value', value)
				this.province = value[0]
				this.city = value[1]
				this.district = value[2]
				if (this.city == '市辖区') {
					this.city = this.province
				}
				this.region = value[0] + value[1] + value[2]
				this.storeAddre = this.province + '/' + this.city
				this.storeAddress = value
				console.log(this.region)

			},
			// 删除城市
			btnCs(){
				this.city = ''
				this.storeAddress = []
				this.storeAddre = '请选择城市'
			},
			//定位获得当前位置信息
			getMyLocation() {
				var geolocation = new qq.maps.Geolocation("G5GBZ-NJ664-5KUUL-FBXE5-PQ5HK-C4BPJ", "充电桩");
				geolocation.getIpLocation(this.showPosition, this.showErr);
				// geolocation.getLocation(this.showPosition, this.showErr);//或者用getLocation精确度比较高
			},
			showPosition(position) {
				console.log(position);
				// this.latitude = position.lat;
				// this.longitude = position.lng;
				// this.city = position.city;
				this.setMap();
			},
			showErr(e) {
				console.log("定位失败", e);
				this.getMyLocation(); //定位失败再请求定位，测试使用
			},
			//位置信息在地图上展示
			setMap() {
				//步骤：定义map变量 调用 qq.maps.Map() 构造函数   获取地图显示容器
				//设置地图中心点
				var myLatlng = new qq.maps.LatLng(this.latitude, this.longitude);
				//定义工厂模式函数
				var myOptions = {
					zoom: 13, //设置地图缩放级别
					center: myLatlng, //设置中心点样式
					mapTypeId: qq.maps.MapTypeId.ROADMAP //设置地图样式详情参见MapType
				}
				// //获取dom元素添加地图信息
				var map = new qq.maps.Map(document.getElementById("container1"), myOptions);
				//给地图添加点击事件

				//给定位的位置添加图片标注
				var marker = new qq.maps.Marker({
					position: myLatlng,
					map: map
				});
				// `````````````
				var that = this;
				if (that.longitude == '') {
					var center = new qq.maps.LatLng(34.34281541842994, 108.93970884382725);
				} else {
					var center = new qq.maps.LatLng(that.latitude, that.longitude);
				}

				var map = new qq.maps.Map(document.getElementById("container1"), {
					center: center,
					zoom: 13
				});
				var marker = new qq.maps.Marker({
					position: center,
					map: map
				});
				var latlngBounds = new qq.maps.LatLngBounds();
				qq.maps.event.addListener(map, "click", function(event) {
					console.log(event, qq.maps);
					that.longitude = event.latLng.getLng(); // 经度
					that.latitude = event.latLng.getLat(); // 纬度

					jsonp('https://apis.map.qq.com/ws/geocoder/v1/?location=' + event.latLng.getLat() + ',' + event
						.latLng.getLng() + '&key=DSQBZ-5MM3P-HEODO-VG6IX-SBRJE-PSBNX&get_poi=1&output=jsonp', {
							myCustomUrlParam: 'veryNice'
						}).then(response => {
						console.log('response', response, response.result.address_component.city)
						that.address = response.result.formatted_addresses.recommend
						this.city = response.result.address_component.city
					}).catch(error => {
						// handle error
					}).then(() => {
						// always executed
					});
					if (markersArray) {
						for (let i in markersArray) {
							markersArray[i].setMap(null);
						}
					}
					if (!marker) {
						marker = new qq.maps.Marker({
							map: map,
							position: event.latLng
						});
					} else {
						marker.setPosition(event.latLng)
					}

					// markersArray.push(marker);

				});
				geocoder = new qq.maps.Geocoder({
					complete: function(result) {
						console.log(result);
						that.longitude = result.detail.location.lng;
						that.latitude = result.detail.location.lat;
						map.setCenter(result.detail.location);
						var marker = new qq.maps.Marker({
							map: map,
							position: result.detail.location
						});
						markersArray.push(marker);
					}
				});
			},
			// 地图定位
			select() {
				console.log(this.address, this.address.replace(/^\s+|\s+$/gm, ''))

				if (this.address == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入详细地址',
						type: 'warning'
					});
					return
				} else {
					var add = this.province + this.city + this.district + this.address
					let that = this
					jsonp('https://apis.map.qq.com/ws/geocoder/v1/?address==' + add +
						'&key=DSQBZ-5MM3P-HEODO-VG6IX-SBRJE-PSBNX&get_poi=1&output=jsonp', {
							myCustomUrlParam: 'veryNice'
						}).then(response => {
						// handle success
						if (response.message == '查询无结果') {
							this.$notify({
								title: '提示',
								duration: 1800,
								message: '详细地址输入有误，请重新输入',
								type: 'warning'
							});
							return
						}
						console.log('response', response)
						that.longitude = response.result.location.lng; // 经度
						that.latitude = response.result.location.lat; // 纬度
						that.city = response.result.address_components.city;
						// that.address = response.result.address_components.province + response.result.address_components
						// 	.city + response.result.address_components.district + response.result.title
						that.setMap()
					}).catch(error => {
						// handle error
					}).then(() => {
						// always executed
					});
				}

			},
			// 获取用户列表弹框
			userselect() {
				this.dialogFormVisible3 = true
				this.tableDataLoading4 = true
				this.userClass()
			},
			// 获取用户列表
			userClass() {
				let phone = -1
				if (this.phone) {
					phone = this.phone
				}
				let nickName = -1
				if (this.nickName) {
					nickName = this.nickName
				}
				this.$http({
					url: this.$http.adornUrl('userCertification/queryUserCertification'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'name': this.nickName2,
						'phone': this.phone1
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading4 = false
					let returnData = data.data
					this.userIds = returnData
				})
			},
			// 查询用户列表
			userclick() {
				this.page = 1
				this.tableDataLoading4 = true
				this.userClass()
			},
			// 重置用户列表数据
			userclose() {
				this.phone1 = ''
				this.nickName2 = ''
				this.tableDataLoading4 = true

				this.userClass()
			},
			// 确定用户
			confirm(row) {
				this.userIdss = row.userId
				this.nickName1 = row.userName
				this.phone = row.phone
				// if (this.nickName == '' || this.nickName == null) {
				// 	this.nickName = row.phone
				// }
				this.dialogFormVisible3 = false
			},
			// 添加标签
			btnTj() {
				if (this.bq == '' || this.bq == ' ') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入标签名',
						type: 'warning'
					});
					return
				} else {
					console.log('this.bq', this.bq)
					this.labels1.push(this.bq)
					this.bq = ''
				}

			},
			// 删除标签
			dels1(index) {
				this.labels1.splice(index, 1);
				console.log(this.labels1)
			},
			// 确定添加
			addmissionNoticeTo() {
				let photost = this.contentImg.toString();
				let certificateImgs = this.certificateImg.toString();
				let workImgs = this.workImg.toString();
				let detailsImgs = this.contentImg.toString();
				let storeAddress1 = this.storeAddress.toString()
				console.log(this.userIdss, this.gameId, photost)
				if (this.gameId == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择项目',
						type: 'warning'
					});
					return
				}
				if (this.userId == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择发布人',
						type: 'warning'
					});
					return
				}

				if (this.headImg == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上主页图',
						type: 'warning'
					});
					return
				}

				if (this.money == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入期望薪资',
						type: 'warning'
					});
					return
				}
				if (this.orderLevel == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入技能',
						type: 'warning'
					});
					return
				}
				if (this.region == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择服务地区',
						type: 'warning'
					});
					return
				}
				if (this.describes == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入描述',
						type: 'warning'
					});
					return
				}

				if (this.city == '') {
					this.select()
				}
				let urls = ''
				if (this.missions == 0) {
					urls = 'orderTaking/insertOrderTaking'
				} else {
					urls = 'orderTaking/updateTakingOrders'
				}
				var times = this.value1 + '~' + this.value2
				let that = this
				setTimeout(function() {
					that.$http({
						url: that.$http.adornUrl(urls),
						method: 'get',
						// data: that.$http.adornData({
						params: that.$http.adornParams({
							'gameId': that.gameId.toString(),
							'myLevel': that.myLevel,
							'orderLevel': that.orderLevel,
							'orderTakingArea': that.orderTakingArea,
							'orderTakingTime': times,
							'oldMoney': that.oldMoney,
							'money': that.money,
							'orderLevel': that.orderLevel,
							'memberMoney': that.memberMoney,
							'region': storeAddress1,
							'describes': that.describes,
							'unit': that.unit,
							'voiceIntroduce': that.voiceIntroduce,
							'headImg': that.headImg,
							'userId': that.userIdss,
							'latitude': that.latitude,
							'longitude': that.longitude,
							'city': that.city,
							'id': that.ids,
							'detailsImg': photost,
							'certificateImg': certificateImgs,
							'workImg': workImgs,
							'detailsImg': detailsImgs,
							'authentication': that.authentication
						})
					}).then(({
						data
					}) => {
						console.log('提交结果', data)
						if (data.code == 0) {
							that.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									that.InformationSelect()
									that.prev()

								}
							})
						} else {
							this.$notify({
								title: '提示',
								duration: 1800,
								message: data.msg,
								type: 'warning'
							});
						}


					})
				}, 1000)
			},
			// 关闭添加信息弹框
			prev() {
				this.dialogFormVisible5 = false
				this.gameId = ''
				this.myLevel = ''
				this.orderTakingTime = ''
				this.value1 = ''
				this.value2 = ''
				this.orderLevel = ''
				this.orderTakingArea = ''
				this.headImg = ''
				this.oldMoney = ''
				this.money = ''
				this.orderLevel = ''
				this.memberMoney = ''
				this.region = ''
				this.describes = ''
				this.unit = '小时'
				this.latitude = ''
				this.longitude = ''
				this.voiceIntroduce = ''
				this.userIdss = ''
				this.city = ''
				this.nickName1 = ''
				this.nickName2 = ''
				this.phone = ''
				this.detailsImg = []
				this.certificateImg = []
				this.workImg = []
				this.detailsImg = []
			},
			// 审核
			shenhe(row) {
				this.userId1 = row.userId
				this.artificerId = row.artificerId
				this.classSelect1(row.artificerId)
				this.dialogFormVisible8 = true
			},
			
			// 查看评价
			plCompile(row) {
				console.log(row)
				this.renwuId = row.artificerId
				this.dialogFormVisible10 = true
				this.pinglunSelect(row.artificerId)
			},
			// 删除信息
			choidelete(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('artificer/deleteArtificer'),
						method: 'post',
						params: this.$http.adornParams({
							'artificerId': row.artificerId
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
									this.InformationSelect()
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
				}).catch(() => {})
			},
			
			// 详情跳转
			updatesvideo(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
			
			// 获取项目列表数据
			classSelect() {
				this.$http({
					url: this.$http.adornUrl('artificer/selectMassageTypePage'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'status': '',
						'sort': '',
						'city': this.citys,
						'authentication': '',
						'by': 1,
						'title': this.title,
						'classifyId':this.classifyId,
						// 'isShopOrHome':this.ordersTypeT
					})
				}).then(({
					data
				}) => {
					console.log('', data)
					if (data.code == 0) {
						this.tableDataLoading = false
						let returnData = data.data
						this.tableData = returnData
						// for (var i in this.tableData.list) {
						// 	if (this.tableData.list[i].contentImg) {
						// 		this.tableData.list[i].contentImg = this.tableData.list[i].contentImg
						// 			.split(',')
						// 	}
						// }
						if (this.tableData.list.length == 0) {
							if(this.page>1){
								this.page = this.page - 1
								this.classSelect()
							}
							
						}

					} else {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: data.msg,
							type: 'warning'
						});
					}
				})
			},
			// 添加项目
			classAdd(row) {
				console.log('row', row)
				if (row != 0) {
					this.titles = '修改项目'
					this.massageTypeTitle = row.title
					this.massageTypeStatus = row.status
					this.sales = row.sales
					this.price = row.price
					this.oldPrice = row.oldPrice
					this.memberPrice = row.memberPrice
					this.artificerPrice = row.artificerPrice
					this.massageTypeId = row.massageTypeId
					if(row.massageImg){
						this.massageImg = row.massageImg
					}else{
						this.massageImg = ''
					}
					
					this.addNum = row.addNum
					this.isSex = row.isSex
					this.applyPeople = row.applyPeople
					this.duration = row.duration
					if(row.contentImg){
						this.contentImg = row.contentImg.split(",")
					}else{
						this.contentImg = []
					}
					
					this.content = row.content
					
					if(row.city!=''&&row.city!='不限'){
						this.city = row.city
						this.storeAddre = this.city
					}else{
						this.city = ''
						this.storeAddre = '请选择城市'
					}
					if(row.labels&&row.labels!==''){
						this.labels1 = row.labels.split(",")
					}else{
						this.labels1 = []
					}
					if(row.isShopOrHome){
						this.ordersType = row.isShopOrHome
					}else{
						this.ordersType = ''
					}
					
					
					this.classType = row.classifyId
					console.log('this.classType',this.classType)
					// this.storeAddress = this.storeAddress.push(this.city)
				} else {
					this.titles = '添加项目'
					this.massageTypeTitle = ''
					this.massageTypeStatus = ''
					this.sales = ''
					this.price = ''
					this.oldPrice = ''
					this.memberPrice = ''
					this.artificerPrice = ''
					this.massageTypeId = ''
					this.massageImg = ''
					this.addNum = ''
					this.isSex = ''
					this.duration = ''
					this.applyPeople = ''
					this.contentImg = []
					this.content = ''
					this.city = ''
					this.labels1 = []
					this.storeAddre = '请选择城市'
					this.classType = ''
					this.ordersType = ''
				}
				this.dialogFormVisible9 = true

			},
			// 启用与否
			change(massageTypeId, status) {
				this.$http({
					url: this.$http.adornUrl(`artificer/updateMassageType`),
					method: 'post',
					data: this.$http.adornData({
						'massageTypeId': massageTypeId,
						'status': status
					})
				}).then(({
					data
				}) => {
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.classSelect()
						}
					})
				})
			},
			// 提交修改、添加项目项目
			refuseto1() {
				
				// if (this.ordersType == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请选择类型',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				// if (this.classType == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请选择项目类型',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				if (this.massageTypeTitle == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入项目名称',
						type: 'warning'
					})
					return
				}
				if (this.massageImg == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传项目图片',
						type: 'warning'
					})
					return
				}
				if (this.contentImg.length == 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传项目详情图片',
						type: 'warning'
					})
					return
				}
				if (this.duration === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入时长',
						type: 'warning'
					})
					return
				}
				if (this.isSex === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择性别要求',
						type: 'warning'
					})
					return
				}
				if (this.applyPeople === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入适用人群',
						type: 'warning'
					})
					return
				}

				if (this.memberPrice === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请会员价',
						type: 'warning'
					})
					return
				}
				if (this.oldPrice === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入原价',
						type: 'warning'
					})
					return
				}
				if (this.price === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入现价',
						type: 'warning'
					})
					return
				}
				if (this.addNum === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入加钟次数',
						type: 'warning'
					})
					return
				}

				if (this.massageTypeStatus === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择状态',
						type: 'warning'
					})
					return
				}
				if(this.city==''){
					var sitys = '不限'
				}else{
					var sitys =this.city
				}
				console.log('this.city',this.city,'sitys',sitys)
				if (this.titles == '添加项目') {
					var urls = 'artificer/insertMassageType'
				} else {
					var urls = 'artificer/updateMassageType'
				}
				this.$http({
					url: this.$http.adornUrl(urls),
					method: 'post',
					// params: this.$http.adornParams({
					data: this.$http.adornData({
						'title': this.massageTypeTitle,
						'status': this.massageTypeStatus,
						'sales': this.sales,
						'price': this.price,
						'oldPrice': this.oldPrice,
						'memberPrice': this.memberPrice,
						'artificerPrice': this.artificerPrice,
						'massageTypeId': this.massageTypeId,
						'massageImg': this.massageImg,
						'isSex': this.isSex,
						'applyPeople':this.applyPeople,
						'duration': this.duration,
						'contentImg': this.contentImg.toString(),
						'content': this.content,
						'city': sitys,
						'labels': this.labels1.toString(),
						'classifyId':this.classType,
						'addNum':this.addNum,
						'isShopOrHome':'',
					})
				}).then(({
					data
				}) => {
					if(data.code==0){
						this.dialogFormVisible9 = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.gameName = ''
								this.classSelect()
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
			},
			// 删除项目项目
			classdelete(row) {
				let delid = row.massageTypeId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('artificer/deleteMassageType'),
						method: 'post',
						params: this.$http.adornParams({
							'massageTypeId': row.massageTypeId
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
									this.classSelect()
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
				}).catch(() => {})
			},
			// 用户评价列表
			pinglunSelect(id) {
				this.userId = this.$store.state.user.id
				this.$http({
					url: this.$http.adornUrl('takingComment/selectOrderTakingComment'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'id': id
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.pinglunData = returnData


				})
			},
			// 删除评价信息
			pinglundelete(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`takingComment/deleteOrderTakingComment/${delid}`),
						method: 'post',
						params: this.$http.adornData({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.pinglunSelect(this.renwuId)
							}
						})
					})
				}).catch(() => {})
			},
			
			// 删除
			clear1(index) {
				this.certificateImg.splice(index, 1);
			},
			clear2(index) {
				this.workImg.splice(index, 1);
			},
			clear3(index) {
				this.contentImg.splice(index, 1);
			},
			
			//上传成功
			handleUploadSuccess1(file, fileList) {
				this.certificateImg.push(file.data)
				console.log('this.certificateImg', this.certificateImg)
			},
			//上传成功
			handleUploadSuccess2(file, fileList) {
				this.workImg.push(file.data)
				console.log('this.workImg', this.workImg)
			},
			//上传成功
			handleUploadSuccess3(file, fileList) {
				this.contentImg.push(file.data)
				console.log('this.contentImg', this.contentImg)
			},
			onprogress1(event, file, fileList) {
				console.log('详情图上传进度', parseInt(event.percent))
				this.percentage1 = parseInt(event.percent)
			},
			// 获取项目项目数据
			classSelect1(artificerId) {
				this.tableDataLoading1 = true
				this.$http({
					url: this.$http.adornUrl('artificer/selectArtificerMassageList'),
					method: 'get',
					params: this.$http.adornParams({
						'artificerId': artificerId,
						'status': ''
					})
				}).then(({
					data
				}) => {
					console.log('```', data)
					if (data.code == 0) {
						this.tableDataLoading1 = false
						let returnData = data.data
						this.tableData3 = returnData
						console.log(this.tableData3)
						// for (var i in this.tableData3) {
						// 	if (this.tableData3[i].contentImg) {
						// 		this.tableData3[i].contentImg = this.tableData3[i].contentImg.split(',')
						// 	}
						// }

					} else {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: data.msg,
							type: 'warning'
						});
					}


				})
			},
			// 项目列表数据
			classSelect2(artificerId) {
				this.$http({
					url: this.$http.adornUrl('artificer/selectMassageTypePage'),
					method: 'get',
					params: this.$http.adornParams({
						'artificerId': artificerId,
						'page': this.page3,
						'limit': this.limit3,
						'status': '',
						'sort': '',
						'city': this.citys,
						'title': this.title,
						'authentication': '',
						'by': 1
					})
				}).then(({
					data
				}) => {
					console.log('````````````', data)
					if (data.code == 0) {
						this.tableDataLoading = false
						let returnData = data.data
						this.tableData4 = returnData
						console.log(this.tableData4.list)
						for (var i in this.tableData4.list) {
							if (this.tableData4.list[i].contentImg) {
								this.tableData4.list[i].contentImg = this.tableData4.list[i].contentImg
									.split(',')
							}
						}

					} else {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: data.msg,
							type: 'warning'
						});
					}
				})
			},
			// 添加项目
			choiadd() {
				this.classSelect2(this.artificerId)
				this.dialogFormVisible11 = true
			},
			// 确认添加
			classListAdd(row) {
				console.log('useridsssssssss', this.userId1)
				this.$http({
					url: this.$http.adornUrl('artificer/insertArtificerMassage'),
					method: 'post',
					params: this.$http.adornParams({
						'userId': this.userId1,
						'artificerMassages': row.massageTypeId
					})
				}).then(({
					data
				}) => {
					console.log('', data)
					if (data.code == 0) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '添加成功',
							type: 'warning'
						});

						this.classSelect1(this.artificerId)
						this.dialogFormVisible11 = false

					} else {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: data.msg,
							type: 'warning'
						});
					}
				})
			},
			// 删除教练项目
			classListdelete(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('artificer/deleteArtificerMassage'),
						method: 'post',
						params: this.$http.adornParams({
							'artificerMassageId': row.artificerMassageId
						})
					}).then(({
						data
					}) => {
						if(data.code==0){
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.classSelect()
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
				}).catch(() => {})
			},
			// 加钟项目弹框
			jiazhongBtn(row){
				this.parentId = row.massageTypeId
				this.classSelectJz()
				this.dialogFormVisibleJz = true
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
			// 获取加钟项目数据
			classSelectJz() {
				this.$http({
					url: this.$http.adornUrl('artificer/selectMassageTypePage'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageJ,
						'limit': this.limitJ,
						'parentId':this.parentId,
						'status': '',
						'sort': '',
						'city': '',
						'authentication': '',
						'by': 1,
						'title': ''
					})
				}).then(({
					data
				}) => {
					console.log('', data)
					if (data.code == 0) {
						this.tableDataLoadingJz = false
						let returnData = data.data
						this.tableDataJz = returnData

					} else {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: data.msg,
							type: 'warning'
						});
					}
				})
			},
			// 添加、修改加钟项目
			classAddJz(row) {
				console.log('row', row)
				if (row != 0) {
					this.titles = '修改加钟项目'
					this.massageTypeTitle = row.title
					this.massageTypeStatus = row.status
					this.sales = row.sales
					this.price = row.price
					this.oldPrice = row.oldPrice
					this.memberPrice = row.memberPrice
					this.artificerPrice = row.artificerPrice
					this.massageTypeId = row.massageTypeId
					this.massageImg = row.massageImg
					// this.isSex = row.isSex
					this.duration = row.duration
					this.contentImg = row.contentImg
					this.content = row.content
					this.city = row.city
					this.labels1 = row.labels.split(",")
					this.storeAddre = this.city
					this.classType=row.classifyId

					// this.storeAddress = this.storeAddress.push(this.city)
				} else {
					this.titles = '添加加钟项目'
					this.massageTypeTitle = ''
					this.massageTypeStatus = ''
					this.sales = ''
					this.price = ''
					this.oldPrice = ''
					this.memberPrice = ''
					this.artificerPrice = ''
					this.massageTypeId = ''
					this.massageImg = ''
					// this.isSex = ''
					this.duration = ''
					this.contentImg = []
					this.content = ''
					this.city = ''
					this.labels1 = []
					this.storeAddre = '请选择城市'
				}
				this.dialogFormVisibleJzA = true
			},
			// 提交修改、添加加钟项目
			refusetoJz() {
				// if (this.classType == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请选择项目类型',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				if (this.massageTypeTitle == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入项目名称',
						type: 'warning'
					})
					return
				}
				if (this.massageImg == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传项目图片',
						type: 'warning'
					})
					return
				}
				// if (this.contentImg.length == 0) {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请上传项目详情图片',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				if (this.duration == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入时长',
						type: 'warning'
					})
					return
				}
				// if (this.isSex === '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请选择性别要求',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				if (this.memberPrice == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请会员价',
						type: 'warning'
					})
					return
				}
				// if (this.artificerPrice == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入教练价',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				if (this.price == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入现价',
						type: 'warning'
					})
					return
				}
				if (this.massageTypeStatus === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择状态',
						type: 'warning'
					})
					return
				}

				if (this.titles == '添加加钟项目') {
					var urls = 'artificer/insertMassageType'
				} else {
					var urls = 'artificer/updateMassageType'
				}
				this.$http({
					url: this.$http.adornUrl(urls),
					method: 'post',
					// params: this.$http.adornParams({
					data: this.$http.adornData({
						'title': this.massageTypeTitle,
						'status': this.massageTypeStatus,
						'sales': this.sales,
						'price': this.price,
						'oldPrice': this.oldPrice,
						'memberPrice': this.memberPrice,
						'artificerPrice': this.artificerPrice,
						'massageTypeId': this.massageTypeId,
						'massageImg': this.massageImg,
						// 'isSex': this.isSex,
						'duration': this.duration,
						'contentImg': this.contentImg.toString(),
						'content': this.content,
						'city': this.city,
						'labels': this.labels1.toString(),
						'classifyId':this.classType,
						'parentId':this.parentId,
					})
				}).then(({
					data
				}) => {
					if(data.code==0){
						this.dialogFormVisibleJzA = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.classSelectJz()
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
			},
			// 删除项目项目
			classdeleteJz(row) {
				let delid = row.massageTypeId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('artificer/deleteMassageType'),
						method: 'post',
						params: this.$http.adornParams({
							'massageTypeId': row.massageTypeId
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
									this.classSelectJz()
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
				}).catch(() => {})
			},
			
			// 信用分明细弹框
			xinyongBtn(row){
				this.artificerId = row.userId
				this.xinyongSelect(this.artificerId)
				this.dialogFormVisibleXyf = true

			},
			// 信用分明细数据
			xinyongSelect(artificerId) {
				this.tableDataLoadingXyf = true
				this.$http({
					url: this.$http.adornUrl('message/selectMessageByUserId'),
					method: 'get',
					params: this.$http.adornParams({
						'userId':artificerId,
						'page':this.page1,
						'limit':this.size1,
						'state':8
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.tableDataLoadingXyf = false
						let returnData = data.data
						this.tableDataXyf = returnData


					} else {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: data.msg,
							type: 'warning'
						});
					}
				})
			},
			// 是否优选
			changeY(val, artificerId) {
				this.$http({
					url: this.$http.adornUrl('artificer/updateArtificers'),
					method: 'post',
					// params: this.$http.adornParams({
					data: this.$http.adornData({
						'artificerId':artificerId,
						'isGoods':val
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
								this.InformationSelect()
							}
						})
					}else{
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {
								this.InformationSelect()
							}
						})
					}

				})
			},
			// 是否热度最高
			changeR(val, artificerId) {
				this.$http({
					url: this.$http.adornUrl('artificer/updateArtificers'),
					method: 'post',
					// params: this.$http.adornParams({
					data: this.$http.adornData({
						'artificerId':artificerId,
						'isHot':val
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
								this.InformationSelect()
							}
						})
					}else{
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {
								this.InformationSelect()
							}
						})
					}

				})
			},
			// 修改销量
			updateBz(row){
				this.ordersId = row.massageTypeId
				this.content = row.sales
				this.dialogFormVisibleBz = true
			},
			// 修改
			replyNoticeTo() {
				
				this.$http({
					url: this.$http.adornUrl('artificer/updateMassageType'),
					method: 'post',
					// params: this.$http.adornParams({
					data: this.$http.adornData({
						'massageTypeId': this.ordersId,
						'sales': this.content,
					})
				}).then(({
					data
				}) => {
					if(data.code==0){
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.classSelect()
							}
						})
						this.dialogFormVisibleBz = false
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
			},
		},
		mounted() {
			// this.InformationSelect()
			// this.homeSelect()
			this.classSelect()
			// this.fwSelect()
		}
	}
</script>

<style>
	.customWidth {
		width: 80% !important;
	}

	.adver_main.box {
		display: block;
		max-width: 100%;
		text-align: center;
		border: 1px dotted rgba(67, 79, 103, .4);
	}

	.cards {
		padding: 0 8px;
		margin-bottom: 15px;
	}

	.adver_main.box a {
		display: flex;
		justify-content: center;
		height: 150px;
		line-height: 150px;
		text-decoration: none
	}

	.bannerManin {
		border: 1px solid #e8e8e8;
		font-size: 14px;
		padding: 0 24px;
		display: flex;
		justify-content: center;
		align-items: center;
		height: 113px;
		color: rgba(0, 0, 0, .65);
	}

	.bannerManin span {
		display: inline-block;
		margin-left: 5px;
	}

	.bannerManin img {
		width: 48px;
		height: 48px;
		border-radius: 50%;
	}

	.bannerbtn {
		display: flex;
		border-top: none !important;
		border: 1px solid #e8e8e8;
		padding: 11px;
		font-size: 14px;
		color: #5cc477;
	}

	.bannerbtn a {
		flex: 1;
		text-align: center;
		color: #5cc477 !important;
		text-decoration: none;
	}

	.imgs {
		position: relative;
		border-radius: 6px;
		width: 148px;
		height: 148px;
		margin-right: 10px;
		display: inline-block;
	}

	.dels {
		position: absolute;
		top: 0;
		left: 0;
		display: none;
	}

	.dels .el-icon-delete {
		line-height: 148px;
		padding-left: 58px;
		font-size: 25px;
		color: #fff;
	}

	.imgs:hover .dels {
		width: 100%;
		height: 100%;
		background: #000;
		display: block;
		opacity: 0.5;
	}

	.bqList {
		padding: 4px 14px;
		margin: 4px;
		border: 1px solid #efefef;
		font-size: 12px;
		color: #999;
		border-radius: 4px;
		margin-right: 15px;
	}

	.delss {
		display: none;
		position: relative;
	}

	.delss .el-icon-delete {
		position: absolute;
		top: 0;
	}

	.bqList:hover .delss {
		display: initial;
		opacity: 0.5;

	}

	.tj {
		padding: 6px !important;
		margin: 4px;
		font-size: 12px;
		border: 1px solid #ccc;
		border-radius: 4px;
	}
</style>

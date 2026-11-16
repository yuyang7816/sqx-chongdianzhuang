<template>
	<div class="nf-page dynamic-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">话题论坛</span>
			<h2 class="nf-page-hero__title">动态管理</h2>
			<p class="nf-page-hero__desc">查看与管理用户动态内容，支持按状态、城市、用户昵称、发布内容等条件筛选，可审核、发布、修改与删除动态。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-search nf-filter-card__head-icon"></i>
				<span>筛选条件</span>
			</div>
			<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
				<el-form-item label="状态">
					<el-select
						class="dynamic-page__filter-select"
						v-model="statusT"
						placeholder="请选择状态"
						@change="selectTrigger()">
						<el-option v-for="item in statusList" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="城市">
					<el-input
						class="dynamic-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入城市"
						v-model="cityT">
					</el-input>
				</el-form-item>
				<el-form-item label="用户昵称">
					<el-input
						class="dynamic-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入用户昵称"
						v-model="nickNameT">
					</el-input>
				</el-form-item>
				<el-form-item label="发布内容">
					<el-input
						class="dynamic-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入发布内容"
						v-model="contentT">
					</el-input>
				</el-form-item>
				<el-form-item label="筛选类型">
					<el-select
						class="dynamic-page__filter-select"
						v-model="typesT"
						placeholder="请选择筛选类型"
						@change="selectTrigger()">
						<el-option v-for="item in typesList" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item>
				<!-- <el-form-item label="是否推荐">
					<el-select
						class="dynamic-page__filter-select"
						v-model="isRecommendT"
						placeholder="请选择是否推荐"
						@change="selectTrigger()">
						<el-option v-for="item in isRecommendList" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item> -->
				<el-form-item label="详细地址">
					<el-input
						class="dynamic-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入详细地址"
						v-model="addressT">
					</el-input>
				</el-form-item>
				<el-form-item class="dynamic-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="select">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
				</el-form-item>
			</el-form>
		</div>

		<el-tabs value="first" class="dynamic-page__tabs">
			<el-tab-pane label="动态列表" name="first">
				<div class="nf-table-card">
					<div class="dynamic-page__toolbar">
						<el-button
							plain
							:disabled="!isAuth('dynamic:add')"
							type="primary"
							icon="el-icon-plus"
							@click="addNoticeDt">发布动态</el-button>
						<el-button
							type="warning"
							plain
							icon="el-icon-delete"
							@click="advertdeletesPldt()"
							:disabled="checkBoxData.length <= 0 || !isAuth('dynamic:delete')">
							批量删除动态
						</el-button>
						<el-button
							type="danger"
							plain
							icon="el-icon-delete"
							@click="advertdeletesPlsj()"
							:disabled="checkBoxData.length <= 0 || !isAuth('dynamic:delete')">
							批量删除数据
						</el-button>
					</div>
					<el-table v-loading="tableDataLoading" :data="guangaoData.records" @selection-change="changeFun">
			<el-table-column type="selection">
			</el-table-column>
			<el-table-column fixed prop="talkId" label="编号" width="50"></el-table-column>
			<el-table-column prop="nickName" label="用户昵称" width="120" fixed="left">
				<template slot-scope="scope">
					<span class="dynamic-page__link"
						@click="updates(scope.row.userId)">{{ scope.row.nickName?scope.row.nickName:'未绑定' }}</span>
				</template>
			</el-table-column>
			<el-table-column prop="imageUrl" label="用户头像" width="100">
				<template slot-scope="scope">
					<img :src="scope.row.imageUrl" alt="" width="60px" height="60">
				</template>
			</el-table-column>
			<!-- <el-table-column prop="circleName" label="圈子名称" width="160">
			</el-table-column> -->
			<el-table-column prop="content" label="发布内容" width="300">
				<template slot-scope="scope">
					<el-popover placement="top-start" title="" trigger="hover">
						<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"
							slot="reference">{{scope.row.content}}</div>
						<div style="width: 300px;">{{scope.row.content}}</div>
					</el-popover>
				</template>
			</el-table-column>
			<el-table-column prop="imgUrl" label="发布图片" width="210">
				<template slot-scope="scope">
					<div v-if="scope.row.img">
						<div v-for="(item,index) in scope.row.img.split(',')"
							style="display: inline-block;width: 60px;">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px;margin-bottom: 10px;" :src="item" alt=""
									slot="reference">
								<img style="width: 300px; height: auto" :src="item" alt="">
							</el-popover>
						</div>
					</div>
					<div v-else>暂无图片</div>
				</template>
			</el-table-column>
			<el-table-column prop="videoUrl" label="发布视频" width="210">
				<template slot-scope="scope">
					<div>
						<video style="width: 100px;height: 100px;" v-if="scope.row.videoUrl" :src="scope.row.videoUrl"
							type="audio/wav" controls="controls"></video>

						<span v-else>暂无视频</span>
					</div>
				</template>
			</el-table-column>
			<el-table-column prop="commentCount" label="评论次数" width="80">
			</el-table-column>
			<el-table-column prop="goodCount" label="点赞次数" width="70"></el-table-column>
			<el-table-column prop="goodNum" label="虚拟点赞次数" width="70" fixed="right">
				<template slot-scope="scope">
					<div>{{scope.row.goodNum?scope.row.goodNum:0}}</div>
					<span class="dynamic-page__link dynamic-page__inline-action"
						@click="updateBtn(scope.row,1)">修改
					</span>
				</template>
			</el-table-column>
			<el-table-column prop="adminBrowseCount" label="总浏览数" width="70"></el-table-column>
			<el-table-column prop="totalBrowseCount" label="真实浏览数" width="70"></el-table-column>
			<el-table-column prop="virtualViewCount" label="虚拟浏览数" width="70" fixed="right">
				<template slot-scope="scope">
					<div>{{scope.row.virtualViewCount?scope.row.virtualViewCount:0}}</div>
					<span class="dynamic-page__link dynamic-page__inline-action"
						@click="updateBtn(scope.row,3)">修改
					</span>
				</template>
			</el-table-column>
			<el-table-column prop="popularValue" label="活力值" width="60" fixed="right">
				<template slot-scope="scope">
					<div>{{scope.row.popularValue?scope.row.popularValue:0}}</div>
					<span class="dynamic-page__link dynamic-page__inline-action"
						@click="updateBtn(scope.row,4)">修改
					</span>
				</template>
			</el-table-column>
			<el-table-column prop="province" label="省份" width="100">
			</el-table-column>
			<el-table-column prop="city" label="城市" width="100">
			</el-table-column>
			<el-table-column prop="district" label="区县" width="100">
			</el-table-column>
			<el-table-column prop="address" label="详细地址" width="160">
			</el-table-column>
			<el-table-column prop="longitude" label="地址经纬度" width="160">
				<template slot-scope="scope">
					<div>经度：{{scope.row.longitude}}</div>
					<div>纬度：{{scope.row.latitude}}</div>
				</template>
			</el-table-column>
			<el-table-column prop="executeTime" label="自动发布时间" width="160">
			</el-table-column>
			<!-- <el-table-column prop="isRecommend" label="是否推荐" width="60" align="center" fixed="right">
				<template slot-scope="scope">
					<el-switch v-model="scope.row.isRecommend" @change="change(scope.row.isRecommend,scope.row.talkId)"
						:active-value="openValue" :inactive-value="closeValue" active-color="#13ce66"
						inactive-color="#ff4949">
					</el-switch>
				</template>
			</el-table-column> -->
			<!-- <el-table-column prop="shareNum" label="转发次数" width="80"></el-table-column> -->
			<el-table-column prop="status" label="状态" width="70" fixed="right">
				<template slot-scope="scope">
					<span v-if="scope.row.status=='1'">待审核</span>
					<span v-if="scope.row.status=='2'">已发布</span>
					<span v-if="scope.row.status=='3'">已下架</span>
					<span v-if="scope.row.status=='4'">待发布</span>

					<span style="color: red;" v-if="scope.row.isDelete==1">(用户端已删除)</span>
				</template>
			</el-table-column>
			<el-table-column prop="auditContent" label="审核/下架理由" width="180"></el-table-column>
			<el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
			<el-table-column label="操作" width="200" fixed="right">
				<template slot-scope="scope">
					<el-button size="mini" type="primary" :disabled="!isAuth('dynamic:shenhe')"
						v-if="scope.row.status=='1'" style="margin: 3px;" @click="advertshenhe(scope.row)">立即审核
					</el-button>
					<el-button size="mini" type="primary" :disabled="!isAuth('dynamic:shenhe')"
						v-if="scope.row.status=='2'" style="margin: 3px;" @click="advertshenhe(scope.row,3)">立即下架
					</el-button>
					<el-button size="mini" type="primary" :disabled="!isAuth('dynamic:shenhe')" plain
						style="margin: 3px;" @click="updateBtnDt(scope.row)">修改信息
					</el-button>
					<el-button size="mini" type="primary" @click="talkAbout(scope.row)" style="margin: 3px;">评论列表
					</el-button>

					<!-- <el-button size="mini" type="primary" :disabled="!isAuth('dynamic:shenhe')"
						v-if="scope.row.status=='2'" style="margin: 3px;" @click="advertPl(scope.row)">添加评论
					</el-button> -->
					<el-button size="mini" type="warning" v-if="scope.row.isDelete!=1"
						:disabled="!isAuth('dynamic:delete')" style="margin: 3px;" @click="advertdeletes1(scope.row)">
						删除动态</el-button>
					<el-button size="mini" type="danger" :disabled="!isAuth('dynamic:delete')" style="margin: 3px;"
						@click="advertdeletes(scope.row)">删除数据</el-button>
					<!-- <el-button size="mini" type="primary" @click="liwuAbout(scope.row)" style="margin: 3px;">礼物榜单
					</el-button> -->
				</template>
			</el-table-column>
		</el-table>
					<div class="dynamic-page__pagination">
						<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
							:page-sizes="[10, 20, 30, 40]" :page-size="size" :current-page="page"
							layout="total,sizes, prev, pager, next,jumper" :total="guangaoData.total">
						</el-pagination>
					</div>
				</div>
			</el-tab-pane>
		</el-tabs>
		<!-- 评论列表 -->
		<el-dialog title="评论列表" :visible.sync="dialogFormVisible1" center width="80%">
			<div style="text-align: right;margin-right: 20px;">
				<el-button style="margin-left:15px;" size="mini" plain type="primary" icon="document"
					@click="selectPl">刷新
				</el-button>
				<el-button style='margin-left:15px;' size="mini" :disabled="!isAuth('dynamic:add')" type="primary"
					icon="document" @click="addNoticewz(1,1)">添加评论</el-button>
				<el-button size="mini" type="danger" icon="document" plain @click="advertdeletesPlsjpl()"
					:disabled="checkBoxData2.length <= 0 || !isAuth('dynamic:delete')">
					批量删除评论
				</el-button>
			</div>
			<el-table v-loading="tableDataLoading1" :data="pinglunData.records" @selection-change="changeFun2">
				<el-table-column type="selection">
				</el-table-column>
				<el-table-column fixed prop="commentId" label="编号" width="80"></el-table-column>
				<el-table-column prop="nickName" label="评论用户" width="100">
					<template slot-scope="scope">
						<span style="color: #5cc477;cursor: pointer;"
							@click="updates(scope.row.userId)">{{ scope.row.nickName?scope.row.nickName:'未绑定' }}</span>
					</template>
				</el-table-column>
				<el-table-column prop="imageUrl" label="用户头像" width="100">
					<template slot-scope="scope">
						<img :src="scope.row.imageUrl" alt="" width="80px" height="80">
					</template>
				</el-table-column>
				<el-table-column prop="isRecommend" label="是否置顶" width="150" align="center">
					<template slot-scope="scope">
						<div style="color: #13ce66;" v-if="scope.row.isRecommend==1">已置顶</div>
						<el-switch v-model="scope.row.isRecommend"
							@change="changeZd(scope.row.isRecommend,scope.row.commentId)" :active-value="openValue"
							:inactive-value="closeValue" active-color="#13ce66" inactive-color="#ff4949">
						</el-switch>
					</template>
				</el-table-column>
				<el-table-column prop="content" label="评论内容" width="200">
				</el-table-column>
				<el-table-column prop="contentImg" label="图片" width="150">
					<template slot-scope="scope">
						<div v-if="scope.row.contentImg == null || scope.row.contentImg == ''">
							暂无图片
						</div>
						<div v-if="scope.row.contentImg" style="display:flex;flex-wrap: wrap;">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px" :src="scope.row.contentImg" alt=""
									slot="reference">
								<img style="width: 200px; height: 200px" :src="scope.row.contentImg" alt="">
							</el-popover>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="province" label="省份">
				</el-table-column>
				<el-table-column prop="goodCount" label="点赞次数" width="100"></el-table-column>
				<el-table-column prop="goodNum" label="虚拟点赞次数" width="100">
					<template slot-scope="scope">
						<div>{{scope.row.goodNum?scope.row.goodNum:0}}</div>
						<el-button size="mini" type="primary" plain :disabled="!isAuth('dynamic:update')"
							style="margin: 5px;" @click="updateBtn(scope.row,2)">修改
						</el-button>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
				<el-table-column label="操作" width="160">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="talkAboutEr(scope.row)" style="margin: 5px;">回复列表
						</el-button>
						<el-button size="mini" plain :disabled="!isAuth('dynamic:add')" type="primary"
							style="margin:5px" icon="document" @click="addNoticewz(scope.row,2)">回复</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('dynamic:delete')" style="margin:5px"
							@click="pldeletes(scope.row)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
					:page-sizes="[10, 20, 30, 40]" :page-size="size1" :current-page="page1"
					layout="total,sizes, prev, pager, next,jumper" :total="pinglunData.total">
				</el-pagination>
			</div>
		</el-dialog>
		<!-- 回复列表 -->
		<el-dialog title="回复列表" :visible.sync="dialogFormVisibleHf" center width="80%">
			<el-button style='margin-left:15px;' size="mini" :disabled="!isAuth('dynamic:add')" type="primary"
				icon="document" @click="addNoticewz(2,3)">回复</el-button>
			<el-table v-loading="tableDataLoadingHf" :data="pinglunDataHf.records">
				<el-table-column prop="commentId" label="编号" width="80"></el-table-column>
				<el-table-column prop="nickName" label="回复用户" width="100">
				</el-table-column>
				<el-table-column prop="imageUrl" label="用户头像" width="100">
					<template slot-scope="scope">
						<img v-if="scope.row.imageUrl" :src="scope.row.imageUrl" alt="" width="60px" height="60">
					</template>
				</el-table-column>
				<el-table-column prop="byNickName" label="回复对象" width="100">
				</el-table-column>
				<el-table-column prop="byImageUrl" label="回复对象头像" width="100">
					<template slot-scope="scope">
						<img v-if="scope.row.byImageUrl" :src="scope.row.byImageUrl" alt="" width="60px" height="60">
					</template>
				</el-table-column>
				<el-table-column prop="content" label="回复内容">
				</el-table-column>
				<el-table-column prop="contentImg" label="图片" width="150">
					<template slot-scope="scope">
						<div v-if="scope.row.contentImg == null || scope.row.contentImg == ''">
							暂无图片
						</div>
						<div v-if="scope.row.contentImg" style="display:flex;flex-wrap: wrap;">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px" :src="scope.row.contentImg" alt=""
									slot="reference">
								<img style="width: 200px; height: 200px" :src="scope.row.contentImg" alt="">
							</el-popover>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
				<el-table-column label="操作" width="150">
					<template slot-scope="scope">
						<el-button size="mini" type="danger" :disabled="!isAuth('dynamic:delete')"
							@click="pldeletesEr(scope.row)">删除</el-button>
						<el-button size="mini" :disabled="!isAuth('dynamic:add')" type="primary" icon="document"
							@click="addNoticewz(scope.row,4)">回复</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChangeHf" @current-change="handleCurrentChangeHf"
					:page-sizes="[10, 20, 30, 40]" :page-size="sizeHf" :current-page="pageHf"
					layout="total,sizes, prev, pager, next,jumper" :total="pinglunDataHf.total">
				</el-pagination>
			</div>
		</el-dialog>
		<!-- 审核弹框 -->
		<el-dialog :title="titles" :visible.sync="dialogFormVisible2" center>
			<div style="margin-bottom: 10px;" v-if="titles=='审核'">
				<span style="width: 200px;display: inline-block;text-align: right;">审核结果：</span>
				<el-radio-group v-model="radio">
					<el-radio :label="2">通过</el-radio>
					<el-radio :label="3">驳回</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span
					style="width: 200px;display: inline-block;text-align: right;position: relative;top: -70px;">理由：</span>
				<el-input style="width:50%;" v-model="content" type="textarea" :rows="4" placeholder="请输入理由">
				</el-input>
			</div>

			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible2 = false">取 消</el-button>
				<el-button type="primary" @click="refuseto()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 修改点赞次数 -->
		<el-dialog title="修改次数" :visible.sync="dialogFormVisibleDz" center>
			<div style="margin-bottom: 10px;" v-if="indexType==1||indexType==2">
				<span style="width: 200px;display: inline-block;text-align: right;">点赞次数：</span>
				<el-input style="width:50%;" v-model="content" type="text" placeholder="请输入点赞次数">
				</el-input>
			</div>
			<div style="margin-bottom: 10px;" v-if="indexType==4">
				<span style="width: 200px;display: inline-block;text-align: right;">活力值：</span>
				<el-input style="width:50%;" v-model="content" type="text" placeholder="请输入活力值">
				</el-input>
			</div>
			<div style="margin-bottom: 10px;" v-if="indexType==3">
				<span style="width: 200px;display: inline-block;text-align: right;">浏览次数：</span>
				<el-input style="width:50%;" v-model="content" type="text" placeholder="请输入浏览次数">
				</el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleDz = false">取 消</el-button>
				<el-button type="primary" @click="refusetoDz()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 添加评论弹框 -->
		<el-dialog title="添加评论" :visible.sync="dialogFormVisiblePl" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">评论数量：</span>
				<el-input style="width:50%;" v-model="num" type="text" placeholder="请输入评论数量">
				</el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">性别：</span>
				<el-radio-group v-model="sex">
					<el-radio :label="1">男</el-radio>
					<el-radio :label="2">女</el-radio>
				</el-radio-group>
			</div>
			<!-- <div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">评论库分类：</span>
				<el-select v-model="virtualCommentClassifyId" placeholder="请选择类型" style="width:50%;">
					<el-option v-for="(item,index) in classifyIds" :key="item.index"
						:label="item.virtualCommentClassifyName" :value="item.virtualCommentClassifyId">
					</el-option>
				</el-select>
			</div> -->
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">评论时间开始：</span>
				<el-date-picker style="width:50%;" v-model="startTime" align="right" type="datetime"
					format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择评论时间开始">
				</el-date-picker>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">评论时间结束：</span>
				<el-date-picker style="width:50%;" v-model="endTime" align="right" type="datetime"
					format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择评论时间结束">
				</el-date-picker>
			</div>

			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisiblePl = false">取 消</el-button>
				<el-button type="primary" @click="refusetoPl()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 添加评论 -->
		<el-dialog :title="titles" :visible.sync="dialogFormVisibleAddPl" center width="80%">

			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">选择用户：</span>
				<el-input style="width:80%;" v-model="userNames" @focus="aiteBtn()" type="text"
					placeholder="请选择选择用户"></el-input>

			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">评论内容：</span>
				<el-input style="width:80%;" v-model="content" type="textarea" :rows="5"
					placeholder="请输入评论内容"></el-input>
				<!-- <quill-editor ref="myTextEditor" v-model="helpWordContent" :options="quillOption"
    			style="padding-bottom: 50px;height: 500px;width: 80%;display: inline-table;margin-bottom: 60px;">
    		</quill-editor> -->
			</div>

			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">图片：</span>
				<div
					style=" display: inline-block;width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
					<el-upload class="avatar-uploader" v-model="contentImg" :action="$http.adornUrlUp('alioss/upload')"
						:show-file-list="false" :on-success="handleAvatarSuccess">
						<img v-if="contentImg" :src="contentImg" class="avatar" style="width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d"></i>
					</el-upload>
				</div>
			</div>

			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleAddPl = false">取 消</el-button>
				<el-button type="primary" @click="releasNoticeTowz()">确 定</el-button>
			</div>
		</el-dialog>
		<el-dialog title="平台用户" :visible.sync="dialogFormVisiblePt" center width="75%">
			<!-- <div style="display: inline-block;margin:10px;">
				<span>地区：</span>
				<el-cascader style="width:150px" size="large" :options="optionsT" ref="cascaderAddr1"
					v-model="storeAddressT" :placeholder="storeAddre" @change="handleChange5" :value="storeAddre">
				</el-cascader>
			</div> -->
			
			<div style="margin:5px;display: inline-block;">
				<span>用户昵称：</span>
				<el-input style="width: 150px;" @keydown.enter.native="selectUser" clearable placeholder="请输入用户昵称"
					v-model="userNameU"></el-input>
				&nbsp;&nbsp;&nbsp;&nbsp;
			</div>
			<div style="margin:5px;display: inline-block;">
				<span>手机号：</span>
				<el-input style="width: 150px;" @keydown.enter.native="selectUser" clearable placeholder="请输入手机号"
					v-model="phoneU"></el-input>
				&nbsp;&nbsp;&nbsp;&nbsp;
			</div>
			<div style="margin:10px;display: inline-block;">
				
				<el-button size="mini" type="primary" @click="selectUser()">查询</el-button>
				<el-button size="mini" type="primary" @click="clearUser()">重置</el-button>
			</div>
			<el-table v-loading="tableDataLoadingPt" :data="tableDataUder.list" height="600px">
				<el-table-column fixed prop="userId" label="编号"></el-table-column>
				<el-table-column fixed prop="userName" label="昵称" width="150">
					<template slot-scope="scope">
						<span style="color: #f56c6c;"
							@click="updates(scope.row.userId)">{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
					</template>
				</el-table-column>
				<el-table-column label="图像">
					<template slot-scope="scope">
						<img v-if="scope.row.avatar==null||scope.row.avatar==''" src="../../assets/img/avatar.png"
							alt="" width="40" height="40">
						<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
					</template>
				</el-table-column>
				<el-table-column prop="sex" label="性别">
					<template slot-scope="scope">
						<span v-if="scope.row.sex==1">男</span>
						<span v-else-if="scope.row.sex==2">女</span>
						<span v-else>未设置</span>
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="手机号">
					<template slot-scope="scope">
						<span style="color: #5cc477;cursor: pointer;">
							{{ scope.row.phone ? scope.row.phone : '未绑定' }}
						</span>
					</template>
				</el-table-column>
				<el-table-column label="操作" width="120">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="xuanze(scope.row)">选择</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChangeU" @current-change="handleCurrentChangeU"
					:page-sizes="[5, 10, 15, 20]" :page-size="limitU" :current-page="pageU"
					layout="total,sizes, prev, pager, next,jumper" :total="tableDataUder.totalCount">
				</el-pagination>
			</div>
		</el-dialog>
		<!-- 发布动态 -->
		<el-dialog title="发布动态" :visible.sync="dialogFormVisibleAddDt" center width="80%">

			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">选择用户：</span>
				<el-input style="width:80%;" v-model="userNames" @focus="aiteBtn1()" type="text"
					placeholder="请选择选择用户"></el-input>

			</div>
			<!-- <div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">圈子：</span>
				<el-select v-model="circleId" placeholder="请选择状态" style="width:80%;">
					<el-option v-for="item in tableDataQz" :key="item.circleId" :label="item.circleName"
						:value="item.circleId">
					</el-option>
				</el-select>
			</div> -->
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">动态内容：</span>
				<el-input style="width:80%;" v-model="content" type="textarea" :rows="5"
					placeholder="请输入评论内容"></el-input>
			</div>
			<div style="margin-bottom: 10px;display: flex;align-items: center;">
				<span style="width: 100px;display: inline-block;text-align: right;">图片：</span>
				<div style="display: flex;flex-wrap: wrap;">
					<div class="imgs" v-for="(item,index) in img" :key="index">
						<img width="100%" class="images" height="100%" :src="item" alt="">
						<span class="dels">
							<i class="el-icon-delete" @click="dels2(index)"></i>
						</span>
					</div>
					<div class="imgs">
						<el-upload :action="$http.adornUrlUp('alioss/upload')" list-type="picture-card"
							:show-file-list="false" :on-success="handleRemove2" v-if="img.length<9" :multiple="true">
							<i class="el-icon-plus"></i>
						</el-upload>
						<el-dialog :visible.sync="dialogVisible">
							<img width="100%" :src="dialogImageUrl" alt="">
						</el-dialog>
					</div>
				</div>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">视频：</span>
				<div style="display: inline-block;">
					<el-upload class="upload-demo" v-model="videoUrl" :action="$http.adornUrlUp('alioss/upload')"
						:show-file-list="false" :on-success="videoSuccess1" :before-upload="beforeVideoUpload"
						:on-progress="handlePreviewV">
						<el-button size="small" type="primary" v-loading="tableDataLoading5">点击上传</el-button>
						<el-input v-if="videoUrl" v-model="videoUrl" style="width:300px;">
						</el-input>
					</el-upload>
				</div>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">状态：</span>
				<el-radio-group v-model="statusType">
					<el-radio :label="2">正常发布</el-radio>
					<el-radio :label="4">待发布</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;" v-if="statusType==4">
				<span style="width: 100px;display: inline-block;text-align: right;">自动发布时间：</span>
				<el-date-picker style="width: 50%;" v-model="executeTime" align="right" type="datetime"
					format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择自动发布时间">
				</el-date-picker>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">台球馆：</span>
				<el-select v-model="store_id" placeholder="请选择台球馆" style="width:50%;" @change="shopClick()">
					<el-option v-for="item in shopData" :key="item.tearoomId" :label="item.tearoomName"
						:value="item.tearoomId">
					</el-option>
				</el-select>
			</div>
			<!-- <div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">地区：</span>
				<el-cascader style="width:50%" size="large" :options="options" ref="cascaderAddr2"
					v-model="storeAddress" :placeholder="storeAddre" @change="handleChange2" :value="storeAddre">
				</el-cascader>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">详细地址：</span>
				<el-input v-model="address" @keydown.enter.native="selectDt" type="text" style="width:50%;"
					placeholder="请输入详细地址">
				</el-input>&nbsp;&nbsp;&nbsp;<el-button size="mini" type="primary" plain
					@click="selectDt()">获取经纬度</el-button>
			</div> -->
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">台球馆地址：</span>
				<el-input v-model="address" type="text" style="width:50%;" disabled
					placeholder="请输入详细地址"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">纬度：</span>
				<el-input v-model="latitude" style="width:50%;" disabled placeholder="请输入纬度"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">经度：</span>
				<el-input v-model="longitude" style="width:50%;" disabled placeholder="请输入经度"></el-input>
			</div>
			<!-- <div>
				<div id="container1" style="width:80%;height:500px;margin-left: 10%;"></div>
			</div> -->

			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleAddDt = false">取 消</el-button>
				<el-button type="primary" @click="releasNoticeDt()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 修改动态 -->
		<el-dialog title="修改动态" :visible.sync="dialogFormVisibleAddDtXg" center width="80%">
			<!-- <div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">圈子：</span>
				<el-select v-model="circleId" placeholder="请选择状态" style="width:80%;">
					<el-option v-for="item in tableDataQz" :key="item.circleId" :label="item.circleName"
						:value="item.circleId">
					</el-option>
				</el-select>
			</div> -->
			<div style="margin-bottom: 10px;position: relative;">
				<span style="width: 100px;display: inline-block;text-align: right;position: absolute;">动态内容：</span>
				<el-input style="width:80%;margin-left: 100px;" v-model="content" type="textarea" :rows="5"
					placeholder="请输入评论内容"></el-input>
			</div>
			<div style="margin-bottom: 10px;display: flex;align-items: center;">
				<span style="width: 100px;display: inline-block;text-align: right;">图片：</span>
				<div style="display: flex;flex-wrap: wrap;">
					<div class="imgs" v-for="(item,index) in img" :key="index">
						<img width="100%" class="images" height="100%" :src="item" alt="">
						<span class="dels">
							<i class="el-icon-delete" @click="dels2(index)"></i>
						</span>
					</div>
					<div class="imgs">
						<el-upload :action="$http.adornUrlUp('alioss/upload')" list-type="picture-card"
							:show-file-list="false" :on-success="handleRemove2" v-if="img.length<10" :limit="9"
							:multiple="true">
							<i class="el-icon-plus"></i>
						</el-upload>
						<el-dialog :visible.sync="dialogVisible">
							<img width="100%" :src="dialogImageUrl" alt="">
						</el-dialog>
					</div>
				</div>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">视频：</span>
				<div style="display: inline-block;">
					<el-upload class="upload-demo" v-model="videoUrl" :action="$http.adornUrlUp('alioss/upload')"
						:show-file-list="false" :on-success="videoSuccess1" :before-upload="beforeVideoUpload"
						:on-progress="handlePreviewV">
						<el-button size="small" type="primary" v-loading="tableDataLoading5">点击上传</el-button>
						<el-input v-if="videoUrl" v-model="videoUrl" style="width:300px;">
						</el-input>
					</el-upload>
				</div>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">状态：</span>
				<el-radio-group v-model="statusType">
					<el-radio :label="2">正常发布</el-radio>
					<el-radio :label="4">待发布</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;" v-if="statusType==4">
				<span style="width: 100px;display: inline-block;text-align: right;">自动发布时间：</span>
				<el-date-picker style="width: 50%;" v-model="executeTime" align="right" type="datetime"
					format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择自动发布时间">
				</el-date-picker>
			</div>
			<!-- <div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">地区：</span>
				<el-cascader style="width:50%" size="large" :options="options" ref="cascaderAddr3"
					v-model="storeAddress" :placeholder="storeAddre" @change="handleChange55" :value="storeAddre">
				</el-cascader>
			</div> -->
			<!-- <div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">详细地址：</span>
				<el-input v-model="address" @keydown.enter.native="selectDt" type="text" style="width:50%;"
					placeholder="请输入详细地址">
				</el-input>&nbsp;&nbsp;&nbsp;<el-button size="mini" type="primary" plain
					@click="selectDt()">获取经纬度</el-button>
			</div> -->
			<!-- <div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">台球馆：</span>
				<el-select v-model="store_id" placeholder="请选择台球馆" style="width:50%;" @change="shopClick()">
					<el-option v-for="item in shopData" :key="item.tearoomId" :label="item.tearoomName"
						:value="item.tearoomId">
					</el-option>
				</el-select>
			</div>
			
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">台球馆地址：</span>
				<el-input v-model="address" type="text" style="width:50%;" disabled
					placeholder="请输入详细地址"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">纬度：</span>
				<el-input v-model="latitude" style="width:50%;" disabled placeholder="请输入纬度"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">经度：</span>
				<el-input v-model="longitude" style="width:50%;" disabled placeholder="请输入经度"></el-input>
			</div> -->
			<!-- <div>
				<div id="container1" style="width:80%;height:500px;margin-left: 10%;"></div>
			</div> -->

			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleAddDtXg = false">取 消</el-button>
				<el-button type="primary" @click="releasNoticeDtXg()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 礼物榜单 -->
		<el-dialog title="礼物榜单" :visible.sync="dialogFormVisibleLw" center width="80%">
			<div>
				<el-row>

					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">礼物总数量</div>
								<div class="text_color">
									<span>{{talkData.giftCount ? talkData.giftCount : 0}}</span>
								</div>
							</div>
						</div>
					</el-col>

					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">活力值总数</div>
								<div class="text_color">
									<span>{{talkData.popularValue ? talkData.popularValue : 0}}</span>
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">总面币</div>
								<div class="text_color">
									<span>{{talkData.totalCoin ? talkData.totalCoin : 0}}</span>
								</div>
							</div>
						</div>
					</el-col>
				</el-row>
			</div>
			<div>
				<div style="margin:5px;display: inline-block;">
					<span>赠送用户：</span>
					<el-input style="width: 150px;" @keydown.enter.native="select2" clearable placeholder="请输入赠送用户"
						v-model="sendUserNameT"></el-input>
					&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="margin:5px;display: inline-block;">
					<span>礼物名称：</span>
					<el-input style="width: 150px;" @keydown.enter.native="select2" clearable placeholder="请输入礼物名称"
						v-model="giftNameT"></el-input>
					&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="margin:5px;display: inline-block;">
					<span>类型筛选：</span>
					<el-select v-model="types2T" placeholder="请选择筛选类型" style="width:150px;" @change="select2()">
						<el-option v-for="item in typesList2" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="select2">查询
				</el-button>
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleans2">重置
				</el-button>
			</div>

			<el-table v-loading="tableDataLoadingLw" :data="liwuData.list">
				<el-table-column prop="commentId" label="编号" width="80">
					<template slot-scope="scope">
						<span>{{scope.$index+1}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="sendUserName" label="赠送用户">
					<template slot-scope="scope">
						<span style="color: #f56c6c;"
							@click="updates(scope.row.sendUserId)">{{ scope.row.sendUserName ? scope.row.sendUserName : '未绑定' }}</span>
					</template>
				</el-table-column>
				<el-table-column prop="avatar" label="用户头像">
					<template slot-scope="scope">
						<img v-if="scope.row.avatar==null||scope.row.avatar==''" src="../../assets/img/avatar.png"
							alt="" width="40" height="40">
						<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
					</template>
				</el-table-column>
				<el-table-column prop="giftName" label="礼物名称">
				</el-table-column>
				<el-table-column prop="giftImg" label="礼物图片">
					<template slot-scope="scope">
						<img v-if="scope.row.giftImg" :src="scope.row.giftImg" alt="" width="60px" height="60">
					</template>
				</el-table-column>
				<el-table-column prop="giftMoney" label="礼物金额">
				</el-table-column>
				<el-table-column prop="popularValue" label="活力值">
					<template slot-scope="scope">
						{{scope.row.popularValue?scope.row.popularValue:'0'}}
					</template>
				</el-table-column>
				<el-table-column prop="sendTime" label="赠送时间"></el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChangeLw" @current-change="handleCurrentChangeLw"
					:page-sizes="[10, 20, 30, 40]" :page-size="size1" :current-page="page1"
					layout="total,sizes, prev, pager, next,jumper" :total="liwuData.total">
				</el-pagination>
			</div>
		</el-dialog>

	</div>
</template>

<script>
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
		data() {
			return {
				page: 1,
				size: 10,
				page1: 1,
				size1: 10,
				formLabelWidth: '200px',
				tableDataLoading: false,
				tableDataLoading1:false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible: false,
				dialogFormVisibleDz: false,
				dialogFormVisiblePl: false,


				guangaoData: {},
				pinglunData: {},
				talkId: '',
				gtitle: '',
				gambitId: '',
				addressT: '',
				cityT: '',
				nickNameT: '',
				contentT: '',
				typesT: '',
				isRecommendT: '',
				statusT: 0,
				statusList: [{
						value: 0,
						label: '全部'
					},
					{
						value: 1,
						label: '待审核'
					},
					{
						value: 2,
						label: '已发布'
					},
					{
						value: 3,
						label: '已下架'
					},
					{
						value: 4,
						label: '待发布'
					},
				],
				typesList: [{
						value: '',
						label: '全部'
					},
					{
						value: 5,
						label: '评论次数'
					},
					{
						value: 4,
						label: '点赞次数'
					},
					{
						value: 6,
						label: '浏览数'
					},
					{
						value: 3,
						label: '活力值'
					},
				],
				isRecommendList: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '是'
					},
					{
						value: null,
						label: '否'
					},
				],
				sendUserNameT: '',
				giftNameT: '',
				types2T: '',
				typesList2: [{
						value: '',
						label: '时间'
					},
					{
						value: 1,
						label: '礼物金额'
					},
					{
						value: 2,
						label: '活力值'
					},
				],
				content: '',
				radio: '',
				titles: '审核',
				dialogFormVisibleHf: false,
				tableDataLoadingHf: false,
				pinglunDataHf: {},
				pageHf: 1,
				sizeHf: 10,
				parentId: 0,
				indexType: 1,
				commentId: '',
				openValue: 1,
				closeValue: 2,
				checkBoxData: [], //多选框选择的值
				checkBoxData2: [],
				classifyIds: [],
				num: '',
				sex: 1,
				virtualCommentClassifyId: '',
				startTime: '',
				endTime: '',
				createTime: '',
				dialogFormVisibleAddPl: false,
				userNames: '',
				userId: '',
				contentImg: '',
				content: '',
				tableDataUder: {},
				limitU: 10,
				pageU: 1,
				userName: '',
				dialogFormVisiblePt: false,
				tableDataLoadingPt: false,
				dialogFormVisibleAddDt: false,
				tableDataLoading5: false,
				videoUrl: '',
				img: [],
				dialogVisible: false,
				dialogImageUrl: [],

				province: '', //省
				city: '', //市
				district: '', //区
				address: '', //详细地址
				latitude: '', //纬度
				longitude: '', //经度
				options: regionData,
				storeAddress: [],
				storeAddre: '请选择城市',
				byUserId: '',
				sexT: '',
				sexList: [{
						id: '',
						title: '全部'
					},
					{
						id: 1,
						title: '男'
					},
					{
						id: 2,
						title: '女'
					}
				],
				optionsT: provinceAndCityData,
				storeAddressT: [],
				cityT: '',
				provinceT: '',
				dialogFormVisibleAddDtXg: false,
				activity: '',
				activityId: '',
				adminBrowseCount: '',
				age: '',
				auditContent: '',
				browseCount: '',
				commentByUserCount: '',
				commentCount: '',
				consortiaId: '',
				createTime: '',
				distance: '',
				followCount: '',
				goodCount: '',
				goodNum: '',
				goodTalkByUserCount: '',
				goodTalkCount: '',
				imageUrl: '',
				isDelete: '',
				isRecommend: '',
				nickName: '',
				rewardByUserCount: '',
				rewardCount: '',
				sex: '',
				shareNum: '',
				status: '',
				videoUrl: '',
				dialogFormVisibleLw: false,
				tableDataLoadingLw: false,
				liwuData: {},
				talkData: {},
				statusType: 2,
				executeTime: '',

				tableDataQz: [],
				pageQz: 1,
				limitQz: 100,
				circleId: '',
				shopData: [],
				store_id:'',
				userNameU:'',
				phoneU:'',

			}
		},
		methods: {
			handleAvatarSuccess(file) {
				this.contentImg = file.data
			},
			handleAvatarSuccess1(file) {
				this.form.imgUrl = file.data
				this.form.linkUrl = this.form.imgUrl
			},
			handleSizeChange(val) {
				this.size = val
				this.guangaoSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.guangaoSelect()
			},
			handleSizeChange1(val) {
				this.size1 = val
				this.pinglunSelect(this.talkId)
			},
			handleCurrentChange1(val) {
				this.page1 = val
				this.pinglunSelect(this.talkId)
			},
			handleSizeChangeHf(val) {
				this.sizeHf = val
				this.pinglunSelectHf()
			},
			handleCurrentChangeHf(val) {
				this.pageHf = val
				this.pinglunSelectHf()
			},
			handleSizeChangeU(val) {
				this.limitU = val;
				this.dataSelectUser()
			},
			handleCurrentChangeU(val) {
				this.pageU = val;
				this.dataSelectUser()
			},
			handleSizeChangeLw(val) {
				this.size1 = val
				this.liwuSelect(this.talkId)
			},
			handleCurrentChangeLw(val) {
				this.page1 = val
				this.liwuSelect(this.talkId)
			},
			// 刷新
			refresh() {
				this.choicenSelect()
				this.handpick()
			},
			// 添加话题弹框
			advertNotice() {
				this.dialogFormVisible = true
			},
			// 添加话题
			advertNoticeTo() {
				if (this.linkUrl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入跳转链接',
						type: 'warning'
					})
					return
				}
				if (this.imgUrl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传话题图',
						type: 'warning'
					})
					return
				}
				this.$http({
					url: this.$http.adornUrl(
						`banner/update/-1?url=${this.linkUrl}&image=${this.imgUrl}&type=${this.type}`),
					method: 'post',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					this.dialogFormVisible = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.imgUrl = ''
							this.linkUrl = ''
							this.guangaoSelect()
						}
					})
				})
			},
			// 谈论列表
			talkAbout(row) {
				this.talkId = row.talkId
				this.page1 = 1
				this.pinglunSelect(this.talkId)
				this.dialogFormVisible1 = true
			},
			// 点击修改
			advertUpdates(rows) {
				this.form.id = rows.id
				this.form.imgUrl = rows.image_url
				this.form.linkUrl = rows.url
				this.dialogFormVisible1 = true
			},
			// 确定修改
			amendadvertTo() {
				if (this.form.linkUrl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入跳转链接',
						type: 'warning'
					})
					return
				}
				if (this.form.imgUrl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传话题图',
						type: 'warning'
					})
					return
				}
				this.$http({
					url: this.$http.adornUrl(
						`banner/update/${this.form.id}?url=${this.form.linkUrl}&image=${this.form.imgUrl}&type=${this.type}`
					),
					method: 'post',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.guangaoSelect()
							this.dialogFormVisible1 = false
						}
					})
				})
			},
			// 删除数据
			advertdeletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`gambit/deleteTalkById`),
						method: 'post',
						params: this.$http.adornParams({
							'talkId': row.talkId
						})
					}).then(({
						data
					}) => {
						if (data.status == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.guangaoSelect()
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
			advertdeletes1(row) {
				this.$confirm(`确定删除此条动态?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`gambit/deleteTalk`),
						method: 'post',
						params: this.$http.adornParams({
							'talkId': row.talkId
						})
					}).then(({
						data
					}) => {
						if (data.status == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.guangaoSelect()
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
			// 删除评论
			pldeletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`gambit/deleteCommentById`),
						method: 'post',
						params: this.$http.adornParams({
							'commentId': row.commentId
						})
					}).then(({
						data
					}) => {
						if (data.status == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.pinglunSelect(this.talkId)
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
			// 获取动态数据
			guangaoSelect() {

				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('gambit/selectTalkPage'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page - 1,
						'size': this.size,
						'gambitId': this.gambitId,
						'isDelete': 1,
						'status': this.statusT,
						'address': this.addressT,
						'city': this.cityT,
						'userName': this.nickNameT,
						'content': this.contentT,
						'sort': this.typesT,
						'isRecommend': this.isRecommendT,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data.page
					this.guangaoData = returnData
				})
			},
			// 详情跳转
			updates(userId) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: userId
					}
				})
			},
			selectPl() {
				this.page1 = 1
				this.pinglunSelect(this.talkId)
			},
			// 获取评论数据
			pinglunSelect(talkId) {
				let gambitId = this.$route.query.gambitId
				this.tableDataLoading1 = true
				this.$http({
					url: this.$http.adornUrl('gambit/selectCommentPageAdmin'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1,
						'limit': this.size1,
						'talkId': talkId,
						'parentId': 0
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading1 = false
					let returnData = data.data
					this.pinglunData = returnData
				})
			},
			selectTrigger() {
				this.page = 1
				this.guangaoSelect()
			},
			select() {
				this.page = 1
				this.guangaoSelect()
			},
			cleans() {
				this.page = 1
				this.gambitId = ''
				this.gtitle = ''
				this.statusT = 0
				this.addressT = ''
				this.cityT = ''
				this.nickNameT = ''
				this.contentT = ''
				this.typesT = ''
				this.isRecommendT = ''
				this.guangaoSelect()
			},
			// 审核
			advertshenhe(row, status) {
				if (status) {
					this.radio = status
					this.content = ''
					this.titles = '下架'
				} else {
					this.radio = ''
					this.content = ''
					this.titles = '审核'
				}
				this.talkId = row.talkId
				this.dialogFormVisible2 = true
			},
			//提交审核
			refuseto() {
				if (this.radio === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择审核结果',
						type: 'warning'
					});
					return
				}
				if (this.radio === 3) {
					if (this.content == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入理由',
							type: 'warning'
						});
						return
					}
				}


				this.$http({
					url: this.$http.adornUrl('gambit/auditTalkStatus'),
					method: 'post',
					params: this.$http.adornParams({
						'talkId': this.talkId,
						'status': this.radio,
						'auditContent': this.content
					})
				}).then(({
					data
				}) => {
					if (data.status == 0) {
						this.dialogFormVisible2 = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.content = ''
								this.radio = ''
								this.guangaoSelect()
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
			// 回复弹框
			talkAboutEr(row) {
				// this.talkId = row.talkId

				this.parentId = row.commentId
				this.pinglunSelectHf()
				this.dialogFormVisibleHf = true
			},
			// 获取回复数据
			pinglunSelectHf() {
				let gambitId = this.$route.query.gambitId
				this.tableDataLoadingHf = true
				this.$http({
					url: this.$http.adornUrl('gambit/selectCommentPageAdmin'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageHf,
						'limit': this.sizeHf,
						'talkId': this.talkId,
						'parentId': this.parentId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingHf = false
					let returnData = data.data
					this.pinglunDataHf = returnData
				})
			},
			// 删除评论
			pldeletesEr(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`gambit/deleteCommentById`),
						method: 'post',
						params: this.$http.adornParams({
							'commentId': row.commentId
						})
					}).then(({
						data
					}) => {
						if (data.status == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.pinglunSelectHf()
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
			// 修改点赞次数
			updateBtn(row, index) {
				this.indexType = index
				if (index == 1 || index == 3 || index == 4) {
					this.talkId = row.talkId
				} else {
					this.commentId = row.commentId
				}

				this.content = ''
				this.dialogFormVisibleDz = true

			},
			refusetoDz() {

				if (this.indexType == 1) {
					if (this.content === '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入点赞次数',
							type: 'warning'
						});
						return
					}
					this.$http({
						url: this.$http.adornUrl('gambit/updateTalkGoodNum'),
						method: 'post',
						params: this.$http.adornParams({
							'talkId': this.talkId,
							'goodNum': this.content
						})
					}).then(({
						data
					}) => {
						if (data.status == 0) {
							this.dialogFormVisibleDz = false
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.content = ''
									this.guangaoSelect()
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
				} else if (this.indexType == 2) {
					if (this.content === '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入点赞次数',
							type: 'warning'
						});
						return
					}
					this.$http({
						url: this.$http.adornUrl('gambit/updateCommentGoodNum'),
						method: 'post',
						params: this.$http.adornParams({
							'commentId': this.commentId,
							'goodNum': this.content
						})
					}).then(({
						data
					}) => {
						if (data.status == 0) {
							this.dialogFormVisibleDz = false
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.content = ''
									this.pinglunSelect(this.talkId)
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
				} else if (this.indexType == 4) {
					if (this.content === '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入活力值',
							type: 'warning'
						});
						return
					}
					this.$http({
						url: this.$http.adornUrl('gambit/editPopular'),
						method: 'post',
						data: this.$http.adornData({
							'talkId': this.talkId,
							'value': this.content
						})
					}).then(({
						data
					}) => {
						if (data.status == 0) {
							this.dialogFormVisibleDz = false
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.content = ''
									this.guangaoSelect()
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
				} else {
					if (this.content === '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入浏览次数',
							type: 'warning'
						});
						return
					}
					this.$http({
						url: this.$http.adornUrl('gambit/updateTalkBrowseCount'),
						method: 'post',
						params: this.$http.adornParams({
							'talkId': this.talkId,
							'adminBrowseCount': this.content
						})
					}).then(({
						data
					}) => {
						if (data.status == 0) {
							this.dialogFormVisibleDz = false
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.content = ''
									this.guangaoSelect()
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
				}

			},
			// 是否推荐
			change(val, id) {
				if (val == 1) {
					var vals = 1
				} else {
					var vals = null
				}
				this.$http({
					url: this.$http.adornUrl('gambit/updateTalkIsRecommend'),
					method: 'post',
					params: this.$http.adornParams({
						'talkId': id,
						'isRecommend': vals
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
								this.guangaoSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {
								this.guangaoSelect()
							}
						})
					}

				})
			},
			// 多选
			changeFun(val) {
				this.checkBoxData = val
			},
			// 批量删除动态
			advertdeletesPldt(id) {
				var ids = id ? [id] : this.checkBoxData.map(item => {
					return item.talkId
				})
				this.$confirm(`确定批量删除动态?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					for (var i in ids) {
						this.$http({
							url: this.$http.adornUrl(`gambit/deleteTalk`),
							method: 'post',
							params: this.$http.adornParams({
								'talkId': ids[i]
							})
						}).then(({
							data
						}) => {
							if (data.status == 0) {
								this.$message({
									message: '删除成功',
									type: 'success',
									duration: 1500,
									onClose: () => {
										this.guangaoSelect()
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
					}

				})
			},
			// 批量删除数据
			advertdeletesPlsj(id) {
				var ids = id ? [id] : this.checkBoxData.map(item => {
					return item.talkId
				})
				this.$confirm(`确定批量删除数据?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					for (var i in ids) {
						this.$http({
							url: this.$http.adornUrl(`gambit/deleteTalkById`),
							method: 'post',
							params: this.$http.adornParams({
								'talkId': ids[i]
							})
						}).then(({
							data
						}) => {
							if (data.status == 0) {
								this.$message({
									message: '删除成功',
									type: 'success',
									duration: 1500,
									onClose: () => {
										this.guangaoSelect()
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
					}

				})
			},
			// 添加评论弹框
			advertPl(row) {
				this.createTime = row.createTime
				this.talkId = row.talkId
				this.num = ''
				this.sex = 1
				this.virtualCommentClassifyId = ''
				this.startTime = ''
				this.endTime = ''
				this.dialogFormVisiblePl = true
				this.dataSelectW()
			},
			refusetoPl() {

				if (this.num === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入评论数量',
						type: 'warning'
					});
					return
				}
				// if (this.virtualCommentClassifyId === '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请选择评论库分类',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				// console.log(new Date(this.createTime).getTime())
				// return
				if (new Date(this.createTime).getTime() > new Date(this.startTime).getTime()) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '评论开始时间不能早于动态发布时间',
						type: 'warning'
					});
					return
				}
				if (this.startTime === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择评论时间开始',
						type: 'warning'
					});
					return
				}
				if (this.endTime === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择评论时间结束',
						type: 'warning'
					});
					return
				}
				if (new Date(this.startTime).getTime() > new Date(this.endTime).getTime()) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '评论时间结束不能早于评论开始时间',
						type: 'warning'
					});
					return
				}
				var sysUserId = this.$cookie.get('sysUserId')
				this.$http({
					url: this.$http.adornUrl('virtualComment/insertTalkComment'),
					method: 'post',
					params: this.$http.adornParams({
						'talkId': this.talkId,
						'num': this.num,
						'sex': this.sex,
						'virtualCommentClassifyId': this.virtualCommentClassifyId,
						'startTime': this.startTime,
						'endTime': this.endTime,
						// 'sysUserId':sysUserId,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisiblePl = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.guangaoSelect()
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
			// 分类无分页
			dataSelectW() {
				this.$http({
					url: this.$http.adornUrl('virtualComment/selectVirtualCommentClassifyList'),
					method: 'get',
					params: this.$http.adornParams({
						'helpClassifyName': ''
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.classifyIds = returnData
				})
			},
			// 评论是否置顶
			changeZd(val, id) {
				this.$http({
					url: this.$http.adornUrl('gambit/updateCommentIsRecommend'),
					method: 'post',
					params: this.$http.adornParams({
						'commentId': id,
						// 'isRecommend':vals
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
								this.pinglunSelect(this.talkId)
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {
								this.pinglunSelect(this.talkId)
							}
						})
					}

				})
			},
			// 添加评论弹框
			addNoticewz(row, index) {
				if (index == 1) {
					this.titles = '添加评论'
					this.parentId = 0
				} else if (index == 2) {
					this.titles = '添加回复'
					this.parentId = row.commentId
				}
				if (index == 4) {
					this.titles = '添加回复'
					this.byUserId = row.userId
				} else {
					this.byUserId = ''
				}
				this.userNames = ''
				this.userId = ''
				this.content = ''
				this.contentImg = ''
				this.dialogFormVisibleAddPl = true
			},
			// 添加评论
			releasNoticeTowz() {
				if (this.content == '' && this.contentImg == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '评论内容和图片不能同时为空！',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('gambit/saveComment'),
					method: 'post',
					data: this.$http.adornData({
						'talkId': this.talkId,
						'content': this.content,
						'contentImg': this.contentImg,
						'userId': this.userId,
						'parentId': this.parentId,
						'byUserId': this.byUserId,
					})
				}).then(({
					data
				}) => {
					if (data.status == 0) {
						this.dialogFormVisibleAddPl = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.userNames = ''
								this.userId = ''
								this.content = ''
								this.contentImg = ''
								this.byUserId = ''
								this.pinglunSelectHf()
								this.pinglunSelect(this.talkId)
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
			// @用户
			aiteBtn() {
				// if (this.userId != '') {
				//   this.$notify({
				//     title: '提示',
				//     duration: 1800,
				//     message: '只能@一位用户！',
				//     type: 'warning'
				//   });
				//   return
				// }
				// this.content = this.content+ '@'
				this.userName = ''
				this.sexT = ''
				this.cityT = ''
				this.provinceT = ''
				this.storeAddres = '请选择城市'
				this.storeAddressT = []
				this.pageU = 1
				this.dataSelectUser()
				this.dialogFormVisiblePt = true
			},
			aiteBtn1() {

				this.userNameU= ''
				this.phoneU= ''
				this.pageU = 1
				this.dataSelectUser()
				this.dialogFormVisiblePt = true
			},
			selectUser() {
				this.pageU = 1
				this.dataSelectUser()
			},
			clearUser() {
				// this.sexT = ''
				// this.cityT = ''
				// this.provinceT = ''
				// this.storeAddres = '请选择城市'
				// this.storeAddressT = []
				this.pageU = 1
				this.userNameU= ''
				this.phoneU= ''
				this.dataSelectUser()
			},
			// 获取平台用户
			dataSelectUser() {
				this.tableDataLoadingPt = true
				this.$http({
					url: this.$http.adornUrl('user/selectUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageU,
						'limit': this.limitU,
						'isVip': '',
						'userName': this.userNameU,
						'inviterCode': '',
						'startTime': '',
						'endTime': '',
						'isAuthentication': '',
						'phone': this.phoneU,
						'status': '',
						'isRate':'',
						'moneyUser': '',
						'vipIndex': '',
						
						
						
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.tableDataUder = returnData
					this.tableDataLoadingPt = false
				})
			},
			// 选择用户
			xuanze(row) {
				this.userNames = row.userName
				this.userId = row.userId
				this.dataSelectQz()
				this.dialogFormVisiblePt = false
			},
			// 发布动态 弹框
			addNoticeDt() {
				this.userNames = ''
				this.userId = ''
				this.content = ''
				this.contentImg = ''
				this.videoUrl = ''
				this.province = ''
				this.city = ''
				this.district = ''
				this.statusType = 2
				this.executeTime = ''
				this.address = ''
				this.latitude = ''
				this.longitude = ''
				this.storeAddress = []
				this.img = []
				this.circleId = ''
				this.storeAddres = '请选择城市'
				// this.getMyLocation()
				this.shopSelect()
				this.dialogFormVisibleAddDt = true
			},
			// 确认发布 动态
			releasNoticeDt() {
				if (this.content == '' && this.contentImg == '' && this.img.length == 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '动态内容、图片和视频不能同时为空！',
						type: 'warning'
					});
					return
				}
				// if (this.circleId == '' || this.circleId == null) {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请选择圈子',
				// 		type: 'warning'
				// 	});
				// 	return
				// }



				if (this.statusType == 4) {
					if (this.executeTime == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请选择自动发布时间',
							type: 'warning'
						});
						return
					}
				}
				// if (this.address == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入详细地址',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				// if (this.latitude == '' || this.longitude == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请获取经纬度',
				// 		type: 'warning'
				// 	});
				// 	return
				// }


				this.$http({
					url: this.$http.adornUrl('gambit/saveTalk'),
					method: 'post',
					data: this.$http.adornData({
						'status': 2,
						'content': this.content,
						'img': this.img.toString(),
						'userId': this.userId,
						'videoUrl': this.videoUrl,
						'province': this.province,
						'city': this.city,
						'district': this.district,
						'address': this.address,
						'latitude': this.latitude,
						'longitude': this.longitude,
						'status': this.statusType,
						'executeTime': this.executeTime,
						'groupPlanId': this.circleId,
					})
				}).then(({
					data
				}) => {
					if (data.status == 0) {
						this.dialogFormVisibleAddDt = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.guangaoSelect()
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
			videoSuccess1(file) {
				console.log(file)
				this.videoUrl = file.data;


				this.tableDataLoading5 = false
			},
			// 验证只能上传视频
			beforeVideoUpload(file) {
				var testmsg = file.name.substring(file.name.lastIndexOf('.') + 1)
				const extension = testmsg === 'mp4'
				if (!extension) {
					this.$message({
						message: "上传文件只能是mp4格式！",
						type: 'error'
					})
				}
				return extension;
			},
			handlePreviewV(file) {
				this.tableDataLoading5 = true
			},
			// 图片上传
			handleRemove2(file, ) {
				console.log('--------', file)
				this.img.push(file.data);
			},
			// 删除图图
			dels2(index) {
				console.log(this.img)
				this.img.splice(index, 1);

			},
			// 获取省市区
			handleChange55(value) {
				value = this.$refs['cascaderAddr3'].currentLabels
				this.province = value[0]
				this.city = value[1]
				this.district = value[2]
				if (this.city == '市辖区') {
					this.city = this.province
				}

			},
			// 获取省市区
			handleChange2(value) {
				value = this.$refs['cascaderAddr2'].currentLabels
				this.province = value[0]
				this.city = value[1]
				this.district = value[2]
				if (this.city == '市辖区') {
					this.city = this.province
				}

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
						.latLng.getLng() + '&key=G5GBZ-NJ664-5KUUL-FBXE5-PQ5HK-C4BPJ&get_poi=1&output=jsonp', {
							myCustomUrlParam: 'veryNice'
						}).then(response => {
						console.log('response', response, response.result.address_component.city)
						that.address = response.result.formatted_addresses.recommend
						that.city = response.result.address_component.city
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
				console.log('-----------------', that.city, )
			},
			// 地图定位
			selectDt() {
				console.log(this.city, )

				// if (this.city == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请在地图上定位获取经纬度',
				// 		type: 'warning'
				// 	});
				// 	return
				// } else {
				var add = this.province + this.city + this.district + this.address
				let that = this
				jsonp('https://apis.map.qq.com/ws/geocoder/v1/?address==' + add +
					'&key=G5GBZ-NJ664-5KUUL-FBXE5-PQ5HK-C4BPJ&get_poi=1&output=jsonp', {
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
				// }

			},
			// --------------
			// 多选
			changeFun2(val) {
				this.checkBoxData2 = val
			},
			// 批量删除评论
			advertdeletesPlsjpl(id) {
				var ids = id ? [id] : this.checkBoxData2.map(item => {
					return item.commentId
				})
				this.$confirm(`确定批量删除评论?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					for (var i in ids) {
						this.$http({
							url: this.$http.adornUrl(`gambit/deleteCommentById`),
							method: 'post',
							params: this.$http.adornParams({
								'commentId': ids[i]
							})
						}).then(({
							data
						}) => {
							if (data.status == 0) {
								this.$message({
									message: '删除成功',
									type: 'success',
									duration: 1500,
									onClose: () => {
										this.pinglunSelect(this.talkId)
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
					}

				})
			},
			// 获取省市区
			handleChange5(value) {
				value = this.$refs['cascaderAddr1'].currentLabels
				this.provinceT = value[0]
				this.cityT = value[1]
				// this.district = value[2]
				if (this.cityT == '市辖区') {
					this.cityT = ''
				}
				this.pageU = 1
				this.selectUser()
			},
			// 修改动态 弹框
			updateBtnDt(row) {
				this.talkId = row.talkId
				this.userId = row.userId
				this.content = row.content
				if (row.img) {
					this.img = row.img.split(',')
				} else {
					this.img = []
				}
				if (row.videoUrl) {
					this.videoUrl = row.videoUrl
				} else {
					this.videoUrl = ''
				}
				if (row.province) {
					this.province = row.province
				} else {
					this.province = ''
				}
				if (row.city) {
					this.city = row.city
				} else {
					this.city = ''
				}
				if (row.district) {
					this.district = row.district
				} else {
					this.district = ''
				}
				this.address = row.address
				this.latitude = row.latitude
				this.longitude = row.longitude
				this.storeAddress = []
				this.storeAddre = this.province + '/' + this.city + '/' + this.district
				// this.getMyLocation()
				this.shopSelect()


				this.activity = row.activity
				this.activityId = row.activityId
				this.adminBrowseCount = row.adminBrowseCount
				this.age = row.age
				this.auditContent = row.auditContent
				this.browseCount = row.browseCount
				this.commentByUserCount = row.commentByUserCount
				this.commentCount = row.commentCount
				this.consortiaId = row.consortiaId
				this.createTime = row.createTime
				this.distance = row.distance
				this.followCount = row.followCount
				this.goodCount = row.goodCount
				this.goodNum = row.goodNum
				this.goodTalkByUserCount = row.goodTalkByUserCount
				this.goodTalkCount = row.goodTalkCount
				this.imageUrl = row.imageUrl
				this.isDelete = row.isDelete
				this.isRecommend = row.isRecommend
				this.nickName = row.nickName
				this.rewardByUserCount = row.rewardByUserCount
				this.rewardCount = row.rewardCount
				this.sex = row.sex
				this.shareNum = row.shareNum
				this.status = row.status
				this.videoUrl = row.videoUrl
				this.statusType = Number(row.status)
				this.executeTime = row.executeTime
				this.circleId = row.groupPlanId
				// this.dataSelectQz()
				this.dialogFormVisibleAddDtXg = true

			},
			// 确认发布 动态
			releasNoticeDtXg() {
				if (this.content == '' && this.contentImg == '' && this.img.length == 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '动态内容、图片和视频不能同时为空！',
						type: 'warning'
					});
					return
				}
				// if (this.province == '' || this.city == '' || this.district == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请选择地区',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				// if (this.address == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入详细地址',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				// if (this.latitude == '' || this.longitude == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请获取经纬度',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				if (this.statusType == 4) {
					if (this.executeTime == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请选择自动发布时间',
							type: 'warning'
						});
						return
					}
				}

				this.$http({
					url: this.$http.adornUrl('gambit/updateTalk'),
					method: 'post',
					data: this.$http.adornData({
						'content': this.content,
						'img': this.img.toString(),
						'userId': this.userId,
						'videoUrl': this.videoUrl,
						'province': this.province,
						'city': this.city,
						'district': this.district,
						'address': this.address,
						'latitude': this.latitude,
						'longitude': this.longitude,
						'talkId': this.talkId,
						'activity': this.activity,
						'activityId': this.activityId,
						'adminBrowseCount': this.adminBrowseCount,
						'age': this.age,
						'auditContent': this.auditContent,
						'browseCount': this.browseCount,
						'commentByUserCount': this.commentByUserCount,
						'commentCount': this.commentCount,
						'consortiaId': this.consortiaId,
						'createTime': this.createTime,
						'distance': this.distance,
						'followCount': this.followCount,
						'goodCount': this.goodCount,
						'goodNum': this.goodNum,
						'goodTalkByUserCount': this.goodTalkByUserCount,
						'goodTalkCount': this.goodTalkCount,
						'imageUrl': this.imageUrl,
						'isDelete': this.isDelete,
						'isRecommend': this.isRecommend,
						'nickName': this.nickName,
						'rewardByUserCount': this.rewardByUserCount,
						'rewardCount': this.rewardCount,
						'sex': this.sex,
						'shareNum': this.shareNum,
						// 'status':this.status,
						'videoUrl': this.videoUrl,
						'status': this.statusType,
						'executeTime': this.executeTime,
						'groupPlanId': this.circleId,
					})
				}).then(({
					data
				}) => {
					if (data.status == 0) {
						this.dialogFormVisibleAddDtXg = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.guangaoSelect()
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
			// 礼物榜单 弹框
			liwuAbout(row) {
				this.talkId = row.talkId
				this.page1 = 1
				this.liwuSelect(this.talkId)
				this.dialogFormVisibleLw = true
			},
			// 获取礼物榜单数据
			liwuSelect(talkId) {
				let gambitId = this.$route.query.gambitId
				this.tableDataLoadingLw = true
				this.$http({
					url: this.$http.adornUrl('talkGift/queryList'),
					method: 'post',
					data: this.$http.adornData({
						'pageNum': this.page1,
						'pageSize': this.size1,
						'talkId': talkId,
						'sendUserName': this.sendUserNameT,
						'giftName': this.giftNameT,
						'sort': this.types2T,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingLw = false
					let returnData = data.data
					this.talkData = returnData.talkData
					this.liwuData = returnData.pageInfo
				})
			},
			select2() {
				this.page1 = 1
				this.liwuSelect(this.talkId)
			},
			cleans2() {
				this.page1 = 1
				this.sendUserNameT = ''
				this.giftNameT = ''
				this.types2T = ''
				this.liwuSelect(this.talkId)
			},
			dataSelect1() {
				if (this.$route.query.status) {
					this.statusT = Number(this.$route.query.status)
				} else {
					this.statusT = 0
				}
				let gambitId = this.$route.query.gambitId
				this.gambitId = gambitId
				this.guangaoSelect()
			},
			// 用户的圈子
			dataSelectQz() {
				this.$http({
					url: this.$http.adornUrl('admin/groupPlan/groupPlanList'),
					method: 'post',
					data: this.$http.adornData({
						'pageNum': this.pageQz,
						'pageSize': this.limitQz,
						'adminUserName': '',
						'circleName': '',
						'circleStatus': '',
						'circleType': '',
						'startTime': '',
						'endTime': '',
						'userId': this.userId
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.tableDataQz = returnData.list
				})
			},
			// 选择台球馆
			shopSelect() {
				this.$http({
					url: this.$http.adornUrl('tearoom/selectTearoomList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 1000,
						'tearoomName': '',
						'city': '',
						'status': '',
					})
				}).then(({
					data
				}) => {
					let returnData = data.data.list;
					this.shopData = returnData
				})
			},
			// 选择桌台
			shopClick() {
				for(var i in this.shopData){
					if(this.store_id==this.shopData[i].tearoomId){
						this.address =this.shopData[i].tearoomAddress
						this.latitude = this.shopData[i].tearoomLatitude
						this.longitude = this.shopData[i].tearoomLongitude
						this.province = this.shopData[i].tearoomProvince
						this.city = this.shopData[i].tearoomCity
						this.district = this.shopData[i].tearoomDistrict
					}
				}
				
				
			},
		},
		mounted() {
			if (this.$route.query.status) {
				this.statusT = Number(this.$route.query.status)
			} else {
				this.statusT = 0
			}
			let gambitId = this.$route.query.gambitId
			this.gambitId = gambitId
			this.guangaoSelect()
		},
		watch: {
			'$route': 'dataSelect1'
		}
	}
</script>

<style scoped>
.dynamic-page__filter-select,
.dynamic-page__filter-input {
	width: 150px;
}

.dynamic-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.dynamic-page__tabs {
	margin-bottom: 0;
}

.dynamic-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.dynamic-page__tabs .nf-table-card {
	margin-bottom: 0;
}

.dynamic-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.dynamic-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.dynamic-page__toolbar {
	display: flex;
	flex-wrap: wrap;
	gap: 12px;
	margin-bottom: 12px;
}

.dynamic-page__link {
	color: #5cc477;
	cursor: pointer;
}

.dynamic-page__link:hover {
	color: #3fae5c;
}

.dynamic-page__inline-action {
	display: inline-block;
	margin: 5px;
}

.dynamic-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.nf-table-card .el-button + .el-button {
	margin-left: 0 !important;
	margin-top: 5px !important;
}

.customWidth {
		width: 80% !important;
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

	.box {
		padding: 44px;
		border: 1px solid #eee;
		margin: 15px 10px;
	}

	.box_num {
		font-size: 14px;
		color: #66b1ff;
	}

	.box_num .box_color {
		color: #333;
		font-size: 14px;
		margin-bottom: 15px;
	}

	.box_num div span {
		font-size: 20px;
		margin-left: 5px;
	}

	.text_color {
		color: #5cc477;
	}

	.text_color span {
		margin-right: 5px;
	}
</style>
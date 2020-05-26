package com.wangtian.message;

import android.os.Bundle;
import android.widget.TextView;

import com.wangtian.message.base.BaseMenuActivity;

public class AboutMeActivity extends BaseMenuActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		setActivity(AboutMeActivity.this, "公司简介");
		left(1);
		init();
	}

	private void init() {
		TextView contact1 = (TextView) findViewById(R.id.contact1);
		TextView contact2 = (TextView) findViewById(R.id.contact2);
		TextView contact3 = (TextView) findViewById(R.id.contact3);
		contact1.setText("      北京宸瑞科技有限公司（简称：宸瑞科技）创立于2008年，注册在北京市中关村科技园区，是公共安全领域专业的数据应用服务提供商，属于技术研发型企业。宸瑞科技本着“科技卫国，创新信安”的信念，服务于公共安全事业，面向全国各省市相关单位，专注于海量数据分析、图形化数据分析、异构数据应用、互联网数据服务、全文资料数据库，为公共安全领域提供可靠的产品及专业的服务，为公共安全领域数据的应用提供全方位一体化的解决方案。");
		contact2.setText("      公司员工近80%为技术研发人员，是一支高素质、高技能的技术研发队伍，大多毕业于985、211工程等国家重点院校，骨干研发人员都具有十年以上数据应用、数据挖掘领域的技术研发经验，在数据仓库、多维分析及数据挖掘、人机智能交互、全息可视化分析等技术方面具有多项发明专利、软件著作权及丰富的技术积累。");
		contact3.setText("      公司为科研开发人员创造良好的创新氛围、先进的科技研发环境，并营造现代企业管理机制和先进的商业运作模式，并通过营造良好的公司内部环境、体制和文化，为公司员工提供广阔的发展空间，进一步吸引优秀人士，构建可持续发展的核心竞争力。");
	}

	
}

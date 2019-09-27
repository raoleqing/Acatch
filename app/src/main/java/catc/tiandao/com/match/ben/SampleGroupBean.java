package catc.tiandao.com.match.ben;
import com.hgdendi.expandablerecycleradapter.BaseExpandableRecyclerViewAdapter;

import java.util.List;

import androidx.annotation.NonNull;

public class SampleGroupBean implements BaseExpandableRecyclerViewAdapter.BaseGroupBean<SampleChildBean> {

    private List<SampleChildBean> mList;
    private String mName;

    public SampleGroupBean(@NonNull List<SampleChildBean> list, @NonNull String name) {
        mList = list;
        mName = name;
    }

    @Override
    public int getChildCount() {
        return mList.size();
    }

    @Override
    public boolean isExpandable() {
        return true;
    }

    public String getName() {
        return mName;
    }

    @Override
    public SampleChildBean getChildAt(int index) {
        return mList.size() <= index ? null : mList.get(index);
    }
}

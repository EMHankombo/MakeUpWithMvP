package com.example.enoch.makeapp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.enoch.makeapp.data.model.ProductModel;
import com.example.enoch.makeapp.data.network.AppDataManager;
import com.example.enoch.makeapp.ui.base.BaseFragment;
import com.example.enoch.makeapp.ui.lipstickList.ILipstickMvpView;
import com.example.enoch.makeapp.ui.lipstickList.LipStickPresenter;
import com.example.enoch.makeapp.ui.utils.rx.AppSchedulerProvider;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;


/**
 * A simple {@link Fragment} subclass.
 */
public class LipstickFragment extends BaseFragment implements ILipstickMvpView{


    RecyclerView recyclerView;

    private LipStickPresenter<ILipstickMvpView> lipstickMvpViewLipStickPresenter;

    public LipstickFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_lipstick, container, false);




        return  view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeRecycler(view);

        lipstickMvpViewLipStickPresenter = new LipStickPresenter<>(new AppDataManager(),new AppSchedulerProvider(),
                new CompositeDisposable());
        lipstickMvpViewLipStickPresenter.onViewPrepared();
        lipstickMvpViewLipStickPresenter.onAttach(this);
    }

    public void initializeRecycler(View view){

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerLip);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

    }

    @Override
    public void onFetchSuccess(List<ProductModel> productModel) {

        Log.i("data",productModel.get(1).getName().toString());

        recyclerView.setAdapter(new MakeAppAdapter(productModel, R.layout.row, getActivity().getApplicationContext(), new onClickListener() {
            @Override
            public void onItemClick(ProductModel productModel) {

                int id = productModel.getId();

                Bundle args = new Bundle();

                args.putInt("id",id);

                Log.i("clickTest","clicked" + productModel.getName());
                ItemDisplayFragment itemFragment = new ItemDisplayFragment();

                itemFragment.setArguments(args);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content,itemFragment).commit();
            }

    }));
    }



    @Override
    public void onError(String message) {
        super.onError(message);

        Log.i("stick",message);
    }



}
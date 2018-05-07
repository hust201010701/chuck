/*
 * Copyright (C) 2017 Jeff Gilfelt.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.readystatesoftware.chuck.internal.ui;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.readystatesoftware.chuck.R;
import com.readystatesoftware.chuck.internal.Utils;
import com.readystatesoftware.chuck.internal.data.HttpTransaction;
import com.yuyh.jsonviewer.library.JsonRecyclerView;

public class TransactionPayloadFragment extends Fragment implements TransactionFragment {

    public static final int TYPE_REQUEST = 0;
    public static final int TYPE_RESPONSE = 1;

    private static final String ARG_TYPE = "type";

    TextView headers;
    TextView body;
    JsonRecyclerView jsonBody;
    SimpleDraweeView image;


    private int type;
    private HttpTransaction transaction;

    public TransactionPayloadFragment() {
    }

    public static TransactionPayloadFragment newInstance(int type) {
        TransactionPayloadFragment fragment = new TransactionPayloadFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_TYPE, type);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getInt(ARG_TYPE);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chuck_fragment_transaction_payload, container, false);
        headers = (TextView) view.findViewById(R.id.headers);
        jsonBody = (JsonRecyclerView) view.findViewById(R.id.jsonBody);
        jsonBody.setTextSize(20);
        body = (TextView) view.findViewById(R.id.body);
        image = (SimpleDraweeView) view.findViewById(R.id.image);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        populateUI();
    }

    @Override
    public void transactionUpdated(HttpTransaction transaction) {
        this.transaction = transaction;
        populateUI();
    }

    private void populateUI() {
        if (isAdded() && transaction != null) {
            switch (type) {
                case TYPE_REQUEST:
                    setText(transaction.getRequestHeadersString(true),
                            transaction.getFormattedRequestBody(), transaction.requestBodyIsPlainText(), transaction.isSuspectedJson(true), transaction.isImage(true), transaction.getUrl());
                    break;
                case TYPE_RESPONSE:
                    setText(transaction.getResponseHeadersString(true),
                            transaction.getFormattedResponseBody(), transaction.responseBodyIsPlainText(), transaction.isSuspectedJson(false), transaction.isImage(false), transaction.getUrl());
                    break;
            }
        }
    }

    private void setText(String headersString, String bodyString, boolean isPlainText, boolean isJson, boolean isImage, String url) {
        headers.setVisibility((TextUtils.isEmpty(headersString) ? View.GONE : View.VISIBLE));
        headers.setText(Html.fromHtml(headersString));
        if (!isPlainText) {
            if (isImage) {
                image.setVisibility(View.VISIBLE);
                body.setVisibility(View.GONE);

                Utils.setAnimatedImageUriToFrescoView(image, Uri.parse(url), this.getContext(), false);

            } else {
                image.setVisibility(View.GONE);
                body.setVisibility(View.VISIBLE);
                body.setText(getString(R.string.chuck_body_omitted));
            }
        } else {
            if (isJson) {
                image.setVisibility(View.GONE);
                body.setVisibility(View.GONE);
                jsonBody.bindJson(bodyString);
            } else {
                image.setVisibility(View.GONE);
                body.setVisibility(View.VISIBLE);
                body.setText(bodyString);
            }
        }
    }




}
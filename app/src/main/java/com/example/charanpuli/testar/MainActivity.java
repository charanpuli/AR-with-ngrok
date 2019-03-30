package com.example.charanpuli.testar;

import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;

public class MainActivity extends AppCompatActivity {
    private ArFragment arFragment;
    public String Asset3d="http://9b20b93a.ngrok.io/BallPark.gltf";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arFragment=(ArFragment)getSupportFragmentManager().findFragmentById(R.id.arfragment);

        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            renderModel(hitResult.createAnchor());
        });
    }

    private void renderModel(Anchor anchor) {
        ModelRenderable.builder()
                .setSource(this,
                        RenderableSource.builder()
                        .setSource(this,Uri.parse(Asset3d),RenderableSource.SourceType.GLTF2)
                        .setScale(1f)
                        .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                        .build()

                        ).setRegistryId(Asset3d)
                        .build()
                .thenAccept(modelRenderable -> {
                    addNodeToScene(modelRenderable,anchor);
                })
                .exceptionally(throwable -> {
                    AlertDialog.Builder builder=new AlertDialog.Builder(this);
                    builder.setMessage(throwable.getMessage())
                            .show();
                    return null;
                });


    }

    private void addNodeToScene(ModelRenderable modelRenderable, Anchor anchor) {

        AnchorNode anchorNode=new AnchorNode(anchor);
        anchorNode.setRenderable(modelRenderable);
        arFragment.getArSceneView().getScene().addChild(anchorNode);
    }
}

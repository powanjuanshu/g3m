package com.glob3.mobile.g3mandroidtestingapplication;

import org.glob3.mobile.generated.AltitudeMode;
import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.BoxShape;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.GInitializationTask;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.IBufferDownloadListener;
import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.SceneJSShapesParser;
import org.glob3.mobile.generated.Shape;
import org.glob3.mobile.generated.ShapesRenderer;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.Vector3D;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.DownloadListener;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

	private G3MWidget_Android _g3mWidget;
	private RelativeLayout _placeHolder;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		final G3MBuilder_Android builder = new G3MBuilder_Android(this);
		builder.getPlanetRendererBuilder().setRenderDebug(true);
		
		final ShapesRenderer sr = new ShapesRenderer();
		builder.addRenderer(sr);

		if (false) { // Testing lights
			sr.addShape(new BoxShape(Geodetic3D.fromDegrees(0, 0, 0),
					AltitudeMode.RELATIVE_TO_GROUND, new Vector3D(1000000,
							1000000, 1000000), (float) 1.0, Color.red(), Color
							.black(), true)); // With normals

			sr.addShape(new BoxShape(Geodetic3D.fromDegrees(0, 180, 0),
					AltitudeMode.RELATIVE_TO_GROUND, new Vector3D(1000000,
							1000000, 1000000), (float) 1.0, Color.blue(), Color
							.black(), true)); // With normals

		}
		
		
		if (false) {
			
			final GInitializationTask initializationTask = new GInitializationTask() {
				
				@Override
				public void run(G3MContext context) {
					
					final IBufferDownloadListener listener = new IBufferDownloadListener() {
						
						@Override
						public void onError(URL url) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onDownload(URL url, IByteBuffer buffer, boolean expired) {
							// TODO Auto-generated method stub
							
							   final Shape shape = SceneJSShapesParser.parseFromBSON(buffer, URL.FILE_PROTOCOL + "2029/" , true, 
									   Geodetic3D.fromDegrees(0, 0, 0), AltitudeMode.ABSOLUTE);
							   
							   sr.addShape(shape);
						}
						
						@Override
						public void onCanceledDownload(URL url, IByteBuffer buffer, boolean expired) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onCancel(URL url) {
							// TODO Auto-generated method stub
							
						}
					};
					
					context.getDownloader().requestBuffer(new URL( URL.FILE_PROTOCOL + "2029/2029.bson" ), 1000, TimeInterval.forever(), true, listener, true);

					
					
				};

				@Override
				public boolean isDone(G3MContext context) {
					// TODO Auto-generated method stub
					return true;
				}
			
			};
			
			builder.setInitializationTask(initializationTask);
			
		}
		



		_g3mWidget = builder.createWidget();
		_placeHolder = (RelativeLayout) findViewById(R.id.g3mWidgetHolder);
		_placeHolder.addView(_g3mWidget);

	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

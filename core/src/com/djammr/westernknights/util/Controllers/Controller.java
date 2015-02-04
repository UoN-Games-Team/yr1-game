package com.djammr.westernknights.util.Controllers;

import com.djammr.westernknights.util.View;

/**
 * Engine MVC Setup:<br/>
 *      view -> Controller -> model<br/>
 *      view - observers (push or pull) -> model<br/>
 * <br/><br/>
 * Your controller should manage adding and removing the View observer with the target model(s) (in setView) to ensure
 * the view doesn't have any unnecessary model references
 */
public interface Controller {

    /** If your view is an observer, you should register the view with target Observables here. Otherwise ignore */
    //void setView(View view);
}
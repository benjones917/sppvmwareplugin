/* Copyright (c) 2017 VMware, Inc. All rights reserved. */

// This is the preferred way to initialize the WEB_PLATFORM SDK since 6.5u1
// For older vSphere Client versions, web-platform.js will be used instead.
if (window.parent.vSphereClientSDK && window.parent.vSphereClientSDK.getWebPlatformApi) {
    // Get the Web Platform API directly from the window parent
    window.WEB_PLATFORM = window.parent.vSphereClientSDK.getWebPlatformApi(window);
} else {
    // No web platform initialization method provided by the sdk, falling back to the old approach
    // It should be a synchronous request in order for the WEB_PLATFORM to be available
    $.ajax({
        url: './js/web-platform.js',
        async: false,
        success: function (data) {
            eval(data);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log('Error downloading or processing web-platform.js', jqXHR, textStatus, errorThrown);
        }
    });
}

PluginUtil = {
    getWebContextPath: function() {
        return window.WEB_PLATFORM.getRootPath() + "/spp";
    },

    getString: function(key, params) {
        return window.WEB_PLATFORM.getString("com_ibm_spp", key, params);
    },

    buildDataUrl: function(objectId, propList) {
        var propStr = propList.toString();
        var dataUrl = PluginUtil.getWebContextPath() +
            "/rest/data/properties/" + objectId + "?properties=" + propStr;
        return dataUrl;
    }
};

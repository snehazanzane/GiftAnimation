/**
 * Copyright (C) 2016 Robinhood Markets, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.giftanimation.animationLib;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;

import com.giftanimation.R;

import java.util.List;
import java.util.Random;

public class CommonClass {
    private static int defaultConfettiSize;
    private static int defaultVelocitySlow;
    private static int defaultVelocityNormal;
    private static int defaultVelocityFast;
    private static int explosionRadius;

    private AnimationManager confettiManager;

    private CommonClass(ViewGroup container) {
        ensureStaticResources(container);
    }

    /** START Pre-configured confetti animations **/

    /**
     * @param container the container viewgroup to host the confetti animation.
     * @param colors the set of colors to colorize the confetti bitmaps.
     * @return the created common confetti object.
     */
    public static CommonClass rainingConfetti(ViewGroup container, Drawable[] colors) {
        final CommonClass commonConfetti = new CommonClass(container);
        final AnimationSource confettiSource = new AnimationSource(0, -defaultConfettiSize,
                container.getWidth(), -defaultConfettiSize);
        commonConfetti.configureRainingConfetti(container, confettiSource, colors);
        return commonConfetti;
    }

    /**
     * Configures a confetti manager that has confetti falling from the provided confetti source.
     *
     * @param container the container viewgroup to host the confetti animation.
     * @param confettiSource the source of the confetti animation.
     * @param colors the set of colors to colorize the confetti bitmaps.
     * @return the created common confetti object.
     */
    public static CommonClass rainingConfetti(ViewGroup container,
                                              AnimationSource confettiSource, Drawable[] colors) {
        final CommonClass commonConfetti = new CommonClass(container);
        commonConfetti.configureRainingConfetti(container, confettiSource, colors);
        return commonConfetti;
    }

    /**
     * Configures a confetti manager that has confetti exploding out in all directions from the
     * provided x and y coordinates.
     *
     * @param container the container viewgroup to host the confetti animation.
     * @param x the x coordinate of the explosion source.
     * @param y the y coordinate of the explosion source.
     * @param colors the set of colors to colorize the confetti bitmaps.
     * @return the created common confetti object.
     */
    public static CommonClass explosion(ViewGroup container, int x, int y, Drawable[] colors) {
        final CommonClass commonConfetti = new CommonClass(container);
        commonConfetti.configureExplosion(container, x, y, colors);
        return commonConfetti;
    }

    private static void ensureStaticResources(ViewGroup container) {
        if (defaultConfettiSize == 0) {
            final Resources res = container.getResources();
            defaultConfettiSize = res.getDimensionPixelSize(R.dimen.default_confetti_size);
            defaultVelocitySlow = res.getDimensionPixelOffset(R.dimen.default_velocity_slow);
            defaultVelocityNormal = res.getDimensionPixelOffset(R.dimen.default_velocity_normal);
            defaultVelocityFast = res.getDimensionPixelOffset(R.dimen.default_velocity_fast);
            explosionRadius = res.getDimensionPixelOffset(R.dimen.default_explosion_radius);
        }
    }

    /** END Pre-configured confetti animations **/

    public AnimationManager getConfettiManager() {
        return confettiManager;
    }

    /**
     * Starts a one-shot animation that emits all of the confetti at once.
     *
     * @return the resulting {@link AnimationManager} that's performing the animation.
     */
    public AnimationManager oneShot() {
        return confettiManager.setNumInitialCount(100)
                .setEmissionDuration(0)
                .animate();
    }

    /**
     * Starts a stream of confetti that animates for the provided duration.
     *
     * @param durationInMillis how long to animate the confetti for.
     * @return the resulting {@link AnimationManager} that's performing the animation.
     */
    public AnimationManager stream(long durationInMillis) {
        return confettiManager.setNumInitialCount(0)
                .setEmissionDuration(durationInMillis)
                .setEmissionRate(10)
                .animate();
    }

    /**
     * Starts an infinite stream of confetti.
     *
     * @return the resulting {@link AnimationManager} that's performing the animation.
     */
    public AnimationManager infinite() {
        return confettiManager.setNumInitialCount(0)
                .setEmissionDuration(AnimationManager.INFINITE_DURATION)
                .setEmissionRate(50)
                .animate();
    }

    private AnimationGenerator getDefaultGenerator(Drawable[] colors) {
        final List<Bitmap> bitmaps = Utils.generateConfettiBitmaps(colors, defaultConfettiSize);
        final int numBitmaps = bitmaps.size();
        return new AnimationGenerator() {
            @Override
            public Animation generateConfetto(Random random) {
                return new BitmapImage(bitmaps.get(random.nextInt(numBitmaps)));
            }
        };
    }

    private void configureRainingConfetti(ViewGroup container, AnimationSource confettiSource,
                                          Drawable[] colors) {
        final Context context = container.getContext();
        final AnimationGenerator generator = getDefaultGenerator(colors);

        confettiManager = new AnimationManager(context, generator, confettiSource, container)
                .setVelocityX(0, defaultVelocitySlow)
                .setVelocityY(defaultVelocityNormal, defaultVelocitySlow)
                .setInitialRotation(180, 180)
                .setRotationalAcceleration(360, 180)
                .setTargetRotationalVelocity(360);
    }

    private void configureExplosion(ViewGroup container, int x, int y, Drawable[] colors) {
        final Context context = container.getContext();
        final AnimationGenerator generator = getDefaultGenerator(colors);
        final AnimationSource confettiSource = new AnimationSource(x, y);

        confettiManager = new AnimationManager(context, generator, confettiSource, container)
                .setTTL(1000)
                .setBound(new Rect(
                        x - explosionRadius, y - explosionRadius,
                        x + explosionRadius, y + explosionRadius
                ))
                .setVelocityX(0, defaultVelocityFast)
                .setVelocityY(0, defaultVelocityFast)
                .enableFadeOut(Utils.getDefaultAlphaInterpolator())
                .setInitialRotation(180, 180)
                .setRotationalAcceleration(360, 180)
                .setTargetRotationalVelocity(360);
    }
}

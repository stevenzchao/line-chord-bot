/*
 * Copyright 2018 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package linechrordbot.controller;

import static java.util.Arrays.asList;

import java.net.URI;
import java.util.function.Supplier;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Button;
import com.linecorp.bot.model.message.flex.component.Button.ButtonHeight;
import com.linecorp.bot.model.message.flex.component.Button.ButtonStyle;
import com.linecorp.bot.model.message.flex.component.Icon;
import com.linecorp.bot.model.message.flex.component.Image;
import com.linecorp.bot.model.message.flex.component.Image.ImageAspectMode;
import com.linecorp.bot.model.message.flex.component.Image.ImageAspectRatio;
import com.linecorp.bot.model.message.flex.component.Image.ImageSize;
import com.linecorp.bot.model.message.flex.component.Separator;
import com.linecorp.bot.model.message.flex.component.Spacer;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.component.Text.TextWeight;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;

public class ExampleFlexMessageSupplier implements Supplier<FlexMessage> {
    @Override
    public FlexMessage get() {
        final Image heroBlock =
                Image.builder()
                     .url(ServletUriComponentsBuilder.fromCurrentContextPath().scheme("https").path("buttons/1.jpg").build().toUri())
                     .size(ImageSize.FULL_WIDTH)
                     .aspectRatio(ImageAspectRatio.R20TO13)
                     .aspectMode(ImageAspectMode.Cover)
                     .action(new URIAction("label", URI.create("http://example.com"), null))
                     .build();

        final Box bodyBlock = createBodyBlock();
        final Box footerBlock = createFooterBlock();
        final Bubble bubble =
                Bubble.builder()
//                	 .header(bodyBlock)
                      .hero(heroBlock)
                      .body(bodyBlock)
                      .footer(footerBlock)
                      .build();

        return new FlexMessage("ALT", bubble);
    }

    private Box createFooterBlock() {
        final Spacer spacer = Spacer.builder().size(FlexMarginSize.SM).build();
//        final Button callAction = Button
//                .builder()
//                .style(ButtonStyle.LINK)
//                .height(ButtonHeight.SMALL)
//                .action(new URIAction("CALL", URI.create("tel:000000"), null))
//                .build();
        final Separator separator = Separator.builder().build();
        final Button websiteAction =
                Button.builder()
                      .style(ButtonStyle.LINK)
                      .height(ButtonHeight.SMALL)
                      .action(new URIAction("WEBSITE", URI.create("https://youtube.com"), null))
                      .build();
        final Button postBackAction =
                Button.builder()
                      .style(ButtonStyle.LINK)
                      .height(ButtonHeight.SMALL)
                      .action(new PostbackAction("和弦查詢", "和弦查詢", "和弦查詢"))
                      .build();

        return Box.builder()
                  .layout(FlexLayout.VERTICAL)
                  .spacing(FlexMarginSize.SM)
                  .contents(asList(spacer, websiteAction, separator, postBackAction))
                  .build();
    }

    private Box createBodyBlock() {
        final Text title =
                Text.builder()
                    .text("歡迎來到和弦機器人")
                    .weight(TextWeight.BOLD)
                    .size(FlexFontSize.XL)
                    .build();

//        final Box review = createReviewBox();

        final Box info = createInfoBox();

        return Box.builder()
                  .layout(FlexLayout.VERTICAL)
                  .contents(asList(title, info))
                  .build();
    }

    private Box createInfoBox() {
//        final Box place = Box
//                .builder()
//                .layout(FlexLayout.BASELINE)
//                .spacing(FlexMarginSize.SM)
//                .contents(asList(
//                        Text.builder()
//                            .text("Place")
//                            .color("#aaaaaa")
//                            .size(FlexFontSize.SM)
//                            .flex(1)
//                            .build(),
//                        Text.builder()
//                            .text("Shinjuku, Tokyo")
//                            .wrap(true)
//                            .color("#666666")
//                            .size(FlexFontSize.SM)
//                            .flex(5)
//                            .build()
//                ))
//                .build();
//        final Box time =
//                Box.builder()
//                   .layout(FlexLayout.BASELINE)
//                   .spacing(FlexMarginSize.SM)
//                   .contents(asList(
//                           Text.builder()
//                               .text("Time")
//                               .color("#aaaaaa")
//                               .size(FlexFontSize.SM)
//                               .flex(1)
//                               .build(),
//                           Text.builder()
//                               .text("10:00 - 23:00")
//                               .wrap(true)
//                               .color("#666666")
//                               .size(FlexFontSize.SM)
//                               .flex(5)
//                               .build()
//                   ))
//                   .build();
    	
        final Box infoTitle = Box
                .builder()
                .layout(FlexLayout.BASELINE)
                .spacing(FlexMarginSize.SM)
                .contents(asList(
                        Text.builder()
                            .text("歡迎來到和弦機器人")
                            .wrap(true)
                            .color("#666666")
                            .size(FlexFontSize.SM)
                            .flex(5)
                            .build()
                ))
                .build();
        
        final Box infoSubtitle = Box
                .builder()
                .layout(FlexLayout.BASELINE)
                .spacing(FlexMarginSize.SM)
                .contents(asList(
                      Text.builder()
                      .text("choose your way to practice!!")
                      .color("#aaaaaa")
                      .size(FlexFontSize.SM)
                      .flex(1)
                      .build()
                ))
                .build();

        return Box.builder()
                  .layout(FlexLayout.VERTICAL)
                  .margin(FlexMarginSize.LG)
                  .spacing(FlexMarginSize.SM)
                  .contents(asList(infoTitle,infoSubtitle))
                  .build();
    }

    private Box createReviewBox() {
        final Icon goldStar =
                Icon.builder().size(FlexFontSize.SM).url(URI.create("https://example.com/gold_star.png")).build();
        final Icon grayStar =
                Icon.builder().size(FlexFontSize.SM).url(URI.create("https://example.com/gray_star.png")).build();
        final Text point =
                Text.builder()
                    .text("4.0")
                    .size(FlexFontSize.SM)
                    .color("#999999")
                    .margin(FlexMarginSize.MD)
                    .flex(0)
                    .build();

        return Box.builder()
                  .layout(FlexLayout.BASELINE)
                  .margin(FlexMarginSize.MD)
                  .contents(asList(goldStar, goldStar, goldStar, goldStar, grayStar, point))
                  .build();
    }
}
